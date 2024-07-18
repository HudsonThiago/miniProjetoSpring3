package com.jeanlima.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoDTO {
    private String descricao;
    private BigDecimal preco;
    private Integer quantidade;
    private BigDecimal total;
}
