package com.jeanlima.springrestapiapp.service;

import com.jeanlima.springrestapiapp.model.Cliente;
import com.jeanlima.springrestapiapp.model.Pedido;
import com.jeanlima.springrestapiapp.rest.dto.PedidoDTO;


public interface ClienteService {
    Cliente atualizarNome(int id, String nome);
    Cliente atualizarCPF(int id, String cpf);
    PedidoDTO ListaPedidos(int idCliente);
    
}
