package com.jeanlima.springrestapiapp.service.impl;

import com.jeanlima.springrestapiapp.enums.StatusPedido;
import com.jeanlima.springrestapiapp.exception.InvalidResponseException;
import com.jeanlima.springrestapiapp.exception.RegraNegocioException;
import com.jeanlima.springrestapiapp.model.*;
import com.jeanlima.springrestapiapp.repository.*;
import com.jeanlima.springrestapiapp.rest.dto.EstoqueDTO;
import com.jeanlima.springrestapiapp.rest.dto.ItemPedidoDTO;
import com.jeanlima.springrestapiapp.rest.dto.PedidoDTO;
import com.jeanlima.springrestapiapp.service.EstoqueService;
import com.jeanlima.springrestapiapp.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstoqueServiceImpl implements EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public List<EstoqueDTO> listar() {
        return estoqueRepository.findAll().stream().map((estoque) -> {
            EstoqueDTO estoqueDTO = new EstoqueDTO();
            estoqueDTO.setQuantidade(estoque.getQuantidade());
            estoqueDTO.setIdProduto(estoque.getProduto().getId());
            return estoqueDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Estoque buscarPorId(int id) {
        return estoqueRepository
                .findById(id)
                .orElseThrow(() -> new InvalidResponseException("Estoque não encontrado"));
    }

    @Override
    public Estoque salvar(EstoqueDTO estoqueDTO) {
        Produto produto = produtoRepository
                .findById(estoqueDTO.getIdProduto())
                .orElseThrow(() -> new InvalidResponseException("Produto não encontrado"));
        Optional<Estoque> estoqueByProduto = estoqueRepository.findByProduto_Id(produto.getId());
        if(estoqueByProduto.isEmpty()) {
            Estoque estoque = new Estoque();
            estoque.setQuantidade(estoqueDTO.getQuantidade());
            estoque.setProduto(produto);
            return estoqueRepository.save(estoque);
        } else {
            throw new InvalidResponseException("Já existe um estoque com este produto cadastrado");
        }
    }

    @Override
    public Estoque atualizar(int id) {
        Estoque estoque = buscarPorId(id);
        Produto produto = produtoRepository
                .findById(estoque.getProduto().getId())
                .orElseThrow(() -> new InvalidResponseException("Produto não encontrado"));
        Optional<Estoque> estoqueByProduto = estoqueRepository.findByProduto_Id(produto.getId());
        if(estoqueByProduto.isEmpty()) {
            return estoqueRepository.save(estoqueByProduto.get());
        } else {
            throw new InvalidResponseException("Já existe um estoque com este produto cadastrado");
        }
    }

    @Override
    public void deletar(int id) {
        Estoque estoque = buscarPorId(id);
        estoqueRepository.delete(estoque);
    }

    @Override
    public EstoqueDTO filtrarPorNomeProduto(String nome) {
        Estoque estoque = estoqueRepository
                .filtrarPorNome(nome)
                .orElseThrow(() -> new InvalidResponseException("Estoque não encontrado"));

        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setIdProduto(estoque.getProduto().getId());
        estoqueDTO.setQuantidade(estoque.getQuantidade());

        return estoqueDTO;
    }

    @Override
    public EstoqueDTO atualizarQuantidade(int idProduto, int quantidade) {
        Optional<Estoque> estoqueByProduto = estoqueRepository
                .findByProduto_Id(idProduto);
        if(estoqueByProduto.isEmpty()) {
            throw new InvalidResponseException("Produto não encontrado");
        }
        estoqueByProduto.get().setQuantidade(quantidade);
        estoqueRepository.save(estoqueByProduto.get());
        EstoqueDTO estoqueDTO = new EstoqueDTO();
        estoqueDTO.setIdProduto(idProduto);
        estoqueDTO.setQuantidade(estoqueByProduto.get().getQuantidade());
        return estoqueDTO;
    }
}
