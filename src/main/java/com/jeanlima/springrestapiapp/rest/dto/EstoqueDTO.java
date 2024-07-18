package com.jeanlima.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueDTO {
    private int quantidade;
    private int idProduto;
}
