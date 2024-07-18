package com.jeanlima.springrestapiapp.service.impl;

import com.jeanlima.springrestapiapp.exception.InvalidResponseException;
import com.jeanlima.springrestapiapp.model.*;
import com.jeanlima.springrestapiapp.repository.*;
import com.jeanlima.springrestapiapp.rest.dto.EstoqueDTO;
import com.jeanlima.springrestapiapp.rest.dto.ItemPedidoDTO;
import com.jeanlima.springrestapiapp.rest.dto.PedidoDTO;
import com.jeanlima.springrestapiapp.service.ClienteService;
import com.jeanlima.springrestapiapp.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private final ClienteRepository clienteRepository;
    @Autowired
    private final EstoqueRepository estoqueRepository;
    @Autowired
    private final ProdutoRepository produtoRepository;
    @Autowired
    private final PedidoRepository pedidoRepository;
    @Autowired
    private final ItemPedidoRepository itemPedidoRepository;


    @Override
    public Cliente atualizarNome(int id, String nome) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if(cliente.isEmpty()) {
            throw new InvalidResponseException("Cliente não encontrado");
        }
        cliente.get().setNome(nome);
        return clienteRepository.save(cliente.get());
    }

    @Override
    public Cliente atualizarCPF(int id, String cpf) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if(cliente.isEmpty()) {
            throw new InvalidResponseException("Cliente não encontrado");
        }
        cliente.get().setCpf(cpf);
        return clienteRepository.save(cliente.get());
    }

    @Override
    public PedidoDTO ListaPedidos(int idCliente) {

        Optional<Cliente> cliente = Optional.ofNullable(clienteRepository
                .findById(idCliente)
                .orElseThrow(() -> new InvalidResponseException("Cliente não encontrado")));

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setNome(cliente.get().getNome());
        pedidoDTO.setCpf(cliente.get().getCpf());
        pedidoDTO.setTotal(BigDecimal.ZERO);
        pedidoDTO.setItems(null);
        List<Pedido> pedidos = pedidoRepository
                .findByCliente_Id(idCliente);
        if(pedidos.size() > 0){
            List<ItemPedidoDTO> listaDeItens = new ArrayList<>();

            pedidos.stream().forEach((pedido)->{
                List<ItemPedido> itens = itemPedidoRepository.findByPedido_Id(pedido.getId());

                itens.stream().forEach((item)->{
                    ItemPedidoDTO dto = new ItemPedidoDTO();
                    Optional<Produto> produto = Optional.ofNullable(produtoRepository
                            .findById(item.getProduto())
                            .orElseThrow(() -> new InvalidResponseException("Produto não encontrado")));
                    dto.setDescricao(produto.get().getDescricao());
                    dto.setPreco(produto.get().getPreco());
                    dto.setQuantidade(item.getQuantidade());
                    BigDecimal totalItem = produto.get().getPreco().multiply(new BigDecimal(item.getQuantidade()));
                    dto.setTotal(totalItem);
                    listaDeItens.add(dto);
                });
            });

            pedidoDTO.setItems(listaDeItens);
            pedidoDTO.setTotal(getTotal(listaDeItens));
        }


        return pedidoDTO;
    }

    private BigDecimal getTotal(List<ItemPedidoDTO> itemPedidoDTO){
        List<BigDecimal> list = (List<BigDecimal>) itemPedidoDTO.stream().map(ItemPedidoDTO::getTotal).toList();

        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
