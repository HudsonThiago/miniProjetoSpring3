package com.jeanlima.springrestapiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeanlima.springrestapiapp.model.ItemPedido;

import java.util.List;
import java.util.Optional;


public interface ItemPedidoRepository extends JpaRepository<ItemPedido,Integer>{
    List<ItemPedido> findByPedido_Id(Integer id);
}
