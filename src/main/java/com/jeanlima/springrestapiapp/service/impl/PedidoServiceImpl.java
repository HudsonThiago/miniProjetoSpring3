package com.jeanlima.springrestapiapp.service.impl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeanlima.springrestapiapp.exception.InvalidResponseException;
import com.jeanlima.springrestapiapp.exception.LimitExceededException;
import com.jeanlima.springrestapiapp.model.*;
import com.jeanlima.springrestapiapp.repository.*;
import com.jeanlima.springrestapiapp.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeanlima.springrestapiapp.enums.StatusPedido;
import com.jeanlima.springrestapiapp.exception.RegraNegocioException;
import com.jeanlima.springrestapiapp.service.PedidoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private final PedidoRepository pedidoRepository;
    @Autowired
    private final ClienteRepository clientesRepository;
    @Autowired
    private final ProdutoRepository produtosRepository;
    @Autowired
    private final ItemPedidoRepository itemsPedidoRepository;
    @Autowired
    private final EstoqueRepository estoqueRepository;

    @Override
    @Transactional
    public PedidoDTO salvar(SavePedidoDTO dto) {
        Cliente cliente = clientesRepository
                .findById(dto.getCliente())
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);

        List<ItemPedidoDTO> ItemPedidoDTO = convertToDto(itemsPedido);

        BigDecimal total = getTotal(ItemPedidoDTO);
        pedido.setTotal(total);

        pedidoRepository.save(pedido);
        debitar(itemsPedido);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setNome(cliente.getNome());
        pedidoDTO.setCpf(cliente.getCpf());
        pedidoDTO.setItems(ItemPedidoDTO);
        pedidoDTO.setTotal(total);

        return pedidoDTO;
    }

    private List<ItemPedidoDTO> convertToDto(List<ItemPedido> itemsPedido) {
        return (List<ItemPedidoDTO>) itemsPedido.stream().map((pedido)->{
            Optional<Produto> produto = Optional.ofNullable(produtosRepository
                    .findById(pedido.getProduto())
                    .orElseThrow(() -> new RegraNegocioException("id do produto não existe.")));

            if(produto.isPresent()){
                int saldo = produto.get().getEstoque().getQuantidade() - pedido.getQuantidade();
                if(saldo < 0){
                    throw new LimitExceededException("Não possui produtos suficientes no estoque");
                }
                ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO();
                itemPedidoDTO.setDescricao(produto.get().getDescricao());
                itemPedidoDTO.setPreco(produto.get().getPreco());
                itemPedidoDTO.setQuantidade(pedido.getQuantidade());
                itemPedidoDTO.setTotal(produto.get().getPreco().multiply(new BigDecimal(pedido.getQuantidade())));

                return itemPedidoDTO;
            }
            return null;
        }).collect(Collectors.toList());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<SaveItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: "+ idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());

    }

    private BigDecimal getTotal(List<ItemPedidoDTO> itemPedidoDTO){
        List<BigDecimal> list = (List<BigDecimal>) itemPedidoDTO.stream().map(ItemPedidoDTO::getTotal).toList();

        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void deletar(int id) {
        Optional<Pedido> pedido = Optional.ofNullable(pedidoRepository
                .findById(id)
                .orElseThrow(() -> new RegraNegocioException("Pedido inválido: ")));
        if(pedido.isPresent()){
            List<ItemPedido> itens = itemsPedidoRepository.findByPedido_Id(pedido.get().getId());

            itens.stream().forEach((item)->{
                itemsPedidoRepository.delete(item);
            });
            pedidoRepository.delete(pedido.get());
        }
    }

    private void debitar(List<ItemPedido> itemsPedido) {
        itemsPedido.stream().forEach((pedido)->{
            Optional<Produto> produto = Optional.ofNullable(produtosRepository
                    .findById(pedido.getProduto())
                    .orElseThrow(() -> new RegraNegocioException("id do produto não existe.")));

            if(produto.isPresent()){
                int saldo = produto.get().getEstoque().getQuantidade() - pedido.getQuantidade();
                if(saldo < 0){
                    throw new LimitExceededException("Não possui produtos suficientes no estoque");
                }

                Optional<Estoque> estoqueByProduto = estoqueRepository.findByProduto_Id(produto.get().getId());
                if(estoqueByProduto.isPresent()){
                    estoqueByProduto.get().setQuantidade(saldo);
                    estoqueRepository.save(estoqueByProduto.get());
                }
            }
        });
    }
}
