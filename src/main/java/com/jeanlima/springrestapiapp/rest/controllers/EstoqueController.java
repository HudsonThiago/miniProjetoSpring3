package com.jeanlima.springrestapiapp.rest.controllers;

import com.jeanlima.springrestapiapp.model.Estoque;
import com.jeanlima.springrestapiapp.model.Produto;
import com.jeanlima.springrestapiapp.repository.EstoqueRepository;
import com.jeanlima.springrestapiapp.repository.ProdutoRepository;
import com.jeanlima.springrestapiapp.rest.dto.EstoqueDTO;
import com.jeanlima.springrestapiapp.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
        private EstoqueService estoqueService;


    @GetMapping("{id}")
    @ResponseStatus(OK)
    public Estoque buscarPorId(@PathVariable Integer id){
        return estoqueService.buscarPorId(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<EstoqueDTO> listar( ){
        return estoqueService.listar();
    }

    @GetMapping("filtro/{nome}")
    @ResponseStatus(OK)
    public EstoqueDTO filtroNomeProduto(@PathVariable String nome){
        return estoqueService.filtrarPorNomeProduto(nome);
    }

    @PatchMapping("atualizarQuantidade/{idProduto}/{quantidade}")
    @ResponseStatus(OK)
    public EstoqueDTO atualizarQuantidade(@PathVariable Integer idProduto, @PathVariable Integer quantidade){
        return estoqueService.atualizarQuantidade(idProduto, quantidade);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Estoque save( @RequestBody EstoqueDTO estoqueDTO ){
        return estoqueService.salvar(estoqueDTO);
    }

    @PutMapping("{id}")
    @ResponseStatus(OK)
    public Estoque update(@PathVariable int id ){
        return estoqueService.atualizar(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id){
        estoqueService.deletar(id);
    }

}
