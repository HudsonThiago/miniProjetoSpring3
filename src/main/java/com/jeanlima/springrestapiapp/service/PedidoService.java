package com.jeanlima.springrestapiapp.service;

import com.jeanlima.springrestapiapp.model.Pedido;
import com.jeanlima.springrestapiapp.rest.dto.PedidoDTO;
import com.jeanlima.springrestapiapp.rest.dto.SavePedidoDTO;


public interface PedidoService {
    PedidoDTO salvar(SavePedidoDTO dto );

    void deletar(int id);
}
