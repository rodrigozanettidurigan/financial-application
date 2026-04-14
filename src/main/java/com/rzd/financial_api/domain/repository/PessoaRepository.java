package com.rzd.financial_api.domain.repository;

import com.rzd.financial_api.domain.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository  extends JpaRepository<Pessoa,Long> {

}
