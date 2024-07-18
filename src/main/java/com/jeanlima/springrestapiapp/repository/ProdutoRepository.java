package com.jeanlima.springrestapiapp.repository;

import com.jeanlima.springrestapiapp.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jeanlima.springrestapiapp.model.Produto;

import java.util.Optional;


public interface ProdutoRepository extends JpaRepository<Produto,Integer>{

    Optional<Produto> findById(int id);
}
