package com.jeanlima.springrestapiapp.service;

import com.jeanlima.springrestapiapp.model.Estoque;
import com.jeanlima.springrestapiapp.model.Pedido;
import com.jeanlima.springrestapiapp.rest.dto.EstoqueDTO;

import java.util.List;


public interface EstoqueService {
    List<EstoqueDTO> listar();
    Estoque buscarPorId(int id);
    Estoque salvar( EstoqueDTO estoque );
    Estoque atualizar( int id );
    void deletar( int id );
    EstoqueDTO filtrarPorNomeProduto(String nome);
    EstoqueDTO atualizarQuantidade(int idProduto, int quantidade);
    
}
