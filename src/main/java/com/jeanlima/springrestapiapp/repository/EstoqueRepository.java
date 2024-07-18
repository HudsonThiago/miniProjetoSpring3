package com.jeanlima.springrestapiapp.repository;

import com.jeanlima.springrestapiapp.model.Estoque;
import com.jeanlima.springrestapiapp.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EstoqueRepository extends JpaRepository<Estoque,Integer>{
    Optional<Estoque> findByProduto_Id(Integer id);
    @Query("select e from Estoque e left join fetch e.produto p where p.descricao like %:nome%")
    Optional<Estoque> filtrarPorNome(@Param("nome") String nome);
    //Optional<Estoque> findByProduto_Descricao(String descricao);
}
