package com.rzd.financial_api.controller;

import com.rzd.financial_api.domain.entity.Pessoa;
import com.rzd.financial_api.domain.repository.PessoaRepository;
import com.rzd.financial_api.event.RecursoCriadoEvent;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaController(PessoaRepository pessoaRepository) {
            this.pessoaRepository = pessoaRepository;
    }

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping                            //Pegue o corpo da requisicao e transforme para esse obj java
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        publisher.publishEvent(new RecursoCriadoEvent(this,response, pessoaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);

        return pessoa.isPresent() ? ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {
        pessoaRepository.deleteById(codigo);
    }
    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
        Optional<Pessoa> pessoaSalva = pessoaRepository.findById(codigo);
        if(pessoaSalva == null) {                   //Esperava pelo menos 1 mas foi encontrado 0
            throw new EmptyResultDataAccessException(1);
        }

        BeanUtils.copyProperties(pessoa,pessoaSalva,"codigo");
        pessoaRepository.save(pessoa);
        return ResponseEntity.ok(pessoa);
    }


}
