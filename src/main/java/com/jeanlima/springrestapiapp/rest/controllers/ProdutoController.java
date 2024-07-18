package com.jeanlima.springrestapiapp.rest.controllers;

import java.math.BigDecimal;
import java.util.List;

import com.jeanlima.springrestapiapp.model.Cliente;
import com.jeanlima.springrestapiapp.rest.dto.ProdutoDTO;
import com.jeanlima.springrestapiapp.rest.dto.SaveProdutoDTO;
import com.jeanlima.springrestapiapp.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.jeanlima.springrestapiapp.model.Produto;
import com.jeanlima.springrestapiapp.repository.ProdutoRepository;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ProdutoService produtoService;
    @PostMapping
    @ResponseStatus(CREATED)
    public ProdutoDTO salvar( @RequestBody SaveProdutoDTO produto ){
        return produtoService.salvar(produto);
    }

    @PatchMapping("atualizarDescricao/{id}/{descricao}")
    @ResponseStatus(OK)
    public ProdutoDTO atualizarDescricao(@PathVariable Integer id, @PathVariable String descricao){
        return produtoService.atualizarDescricao(id, descricao);
    }

    @PatchMapping("atualizarPreco/{id}/{preco}")
    @ResponseStatus(OK)
    public ProdutoDTO atualizarPreco(@PathVariable Integer id, @PathVariable BigDecimal preco){
        return produtoService.atualizarPreco(id, preco);
    }

    @PutMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void update( @PathVariable Integer id, @RequestBody Produto produto ){
        repository
                .findById(id)
                .map( p -> {
                   produto.setId(p.getId());
                   repository.save(produto);
                   return produto;
                }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id){
        repository
                .findById(id)
                .map( p -> {
                    repository.delete(p);
                    return Void.TYPE;
                }).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado."));
    }

    @GetMapping("{id}")
    public ProdutoDTO getById(@PathVariable Integer id){
        return produtoService.listarPorId(id);
    }

    @GetMapping
    public List<ProdutoDTO> listar(){
        return produtoService.listar();
    }
}
