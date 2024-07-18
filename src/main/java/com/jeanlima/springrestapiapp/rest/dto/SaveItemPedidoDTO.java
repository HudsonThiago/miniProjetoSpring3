package com.jeanlima.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveItemPedidoDTO {
    private Integer produto;
    private Integer quantidade;
}
