package com.rzd.financial_api.controller;

import com.rzd.financial_api.domain.entity.Lancamento;
import com.rzd.financial_api.domain.repository.LancamentoRepository;
import com.rzd.financial_api.event.RecursoCriadoEvent;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {


    private final LancamentoRepository lancamentoRepository;
    private final ApplicationEventPublisher publisher;

    public LancamentoController(LancamentoRepository lancamentoRepository, ApplicationEventPublisher publisher) {
        this.lancamentoRepository = lancamentoRepository;
        this.publisher = publisher;
    }

//    @Autowired
//    private LancamentoService lancamentoService;

    @GetMapping
    public List<Lancamento> listar() {

        return lancamentoRepository.findAll();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

        publisher.publishEvent(new RecursoCriadoEvent(this,response, lancamentoSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        return lancamentoRepository.findById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
