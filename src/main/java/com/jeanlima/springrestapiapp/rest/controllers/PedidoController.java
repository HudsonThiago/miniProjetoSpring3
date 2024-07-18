
package com.jeanlima.springrestapiapp.rest.controllers;

import com.jeanlima.springrestapiapp.rest.dto.PedidoDTO;
import com.jeanlima.springrestapiapp.rest.dto.SavePedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.jeanlima.springrestapiapp.model.Pedido;
import com.jeanlima.springrestapiapp.service.PedidoService;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDTO save(@RequestBody SavePedidoDTO dto ){
        return pedidoService.salvar(dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id){
        pedidoService.deletar(id);
    }
}
