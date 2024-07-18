package com.jeanlima.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private String nome;
    private String cpf;
    private BigDecimal total;
    private List<ItemPedidoDTO> items;
}
