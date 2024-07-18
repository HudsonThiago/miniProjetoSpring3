package com.jeanlima.springrestapiapp.service.impl;

import com.jeanlima.springrestapiapp.exception.InvalidResponseException;
import com.jeanlima.springrestapiapp.model.Cliente;
import com.jeanlima.springrestapiapp.model.Estoque;
import com.jeanlima.springrestapiapp.model.Produto;
import com.jeanlima.springrestapiapp.repository.ClienteRepository;
import com.jeanlima.springrestapiapp.repository.EstoqueRepository;
import com.jeanlima.springrestapiapp.repository.ProdutoRepository;
import com.jeanlima.springrestapiapp.rest.dto.EstoqueDTO;
import com.jeanlima.springrestapiapp.rest.dto.ProdutoDTO;
import com.jeanlima.springrestapiapp.rest.dto.SaveProdutoDTO;
import com.jeanlima.springrestapiapp.service.ClienteService;
import com.jeanlima.springrestapiapp.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private final ProdutoRepository produtoRepository;

    @Autowired
    private final EstoqueRepository estoqueRepository;

    @Override
    public List<ProdutoDTO> listar() {

        return (List<ProdutoDTO>) produtoRepository.findAll().stream().map((produto) -> {
            Optional<Estoque> estoque = estoqueRepository.findByProduto_Id(produto.getId());
            ProdutoDTO produtoDTO = new ProdutoDTO();
            produtoDTO.setId(produto.getId());
            produtoDTO.setDescricao(produto.getDescricao());
            produtoDTO.setPreco(produto.getPreco());
            if(estoque.isPresent()) produtoDTO.setEstoque(estoque.get().getQuantidade());
            else produtoDTO.setEstoque(0);
            return produtoDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public ProdutoDTO listarPorId(int id) {
        Optional<Produto> produto = Optional.ofNullable(produtoRepository
                .findById(id)
                .orElseThrow(() -> new InvalidResponseException("Produto não encontrado")));
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(produto.get().getId());
        produtoDTO.setDescricao(produto.get().getDescricao());
        produtoDTO.setPreco(produto.get().getPreco());
        if(produto.get().getEstoque() != null) produtoDTO.setEstoque(produto.get().getEstoque().getQuantidade());
        else produtoDTO.setEstoque(0);

        return produtoDTO;
    }

    @Override
    public ProdutoDTO salvar(SaveProdutoDTO saveProdutoDTO) {
        if(saveProdutoDTO.getEstoque()<=0){
            throw new InvalidResponseException("Estoque precisa ser maior do que 0");
        }
        Produto produto = new Produto();
        produto.setDescricao(saveProdutoDTO.getDescricao());
        produto.setPreco(saveProdutoDTO.getPreco());
        Produto produtoSalvo = produtoRepository.save(produto);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(saveProdutoDTO.getEstoque());
        estoque.setProduto(produtoSalvo);
        estoqueRepository.save(estoque);

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(produtoSalvo.getId());
        produtoDTO.setDescricao(saveProdutoDTO.getDescricao());
        produtoDTO.setPreco(saveProdutoDTO.getPreco());
        produtoDTO.setEstoque(saveProdutoDTO.getEstoque());
        return produtoDTO;
    }

    @Override
    public ProdutoDTO atualizarDescricao(int id, String descricao) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if(produto.isEmpty()) {
            throw new InvalidResponseException("Cliente não encontrado");
        }
        produto.get().setDescricao(descricao);
        produtoRepository.save(produto.get());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(produto.get().getId());
        produtoDTO.setDescricao(produto.get().getDescricao());
        produtoDTO.setPreco(produto.get().getPreco());
        produtoDTO.setEstoque(produto.get().getEstoque().getQuantidade());

        return produtoDTO;
    }

    @Override
    public ProdutoDTO atualizarPreco(int id, BigDecimal preco) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if(produto.isEmpty()) {
            throw new InvalidResponseException("Cliente não encontrado");
        }
        produto.get().setPreco(preco);
        produtoRepository.save(produto.get());

        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId(produto.get().getId());
        produtoDTO.setDescricao(produto.get().getDescricao());
        produtoDTO.setPreco(produto.get().getPreco());
        produtoDTO.setEstoque(produto.get().getEstoque().getQuantidade());

        return produtoDTO;
    }
}
