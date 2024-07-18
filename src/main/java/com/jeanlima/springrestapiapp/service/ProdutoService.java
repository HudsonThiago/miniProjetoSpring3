package com.jeanlima.springrestapiapp.service;

import com.jeanlima.springrestapiapp.model.Cliente;
import com.jeanlima.springrestapiapp.model.Estoque;
import com.jeanlima.springrestapiapp.model.Produto;
import com.jeanlima.springrestapiapp.rest.dto.EstoqueDTO;
import com.jeanlima.springrestapiapp.rest.dto.ProdutoDTO;
import com.jeanlima.springrestapiapp.rest.dto.SaveProdutoDTO;

import java.math.BigDecimal;
import java.util.List;


public interface ProdutoService {

    List<ProdutoDTO> listar();
    ProdutoDTO listarPorId(int id);
    ProdutoDTO salvar(SaveProdutoDTO produtoDTO );
    ProdutoDTO atualizarDescricao(int id, String descricao);
    ProdutoDTO atualizarPreco(int id, BigDecimal preco);
    
}
