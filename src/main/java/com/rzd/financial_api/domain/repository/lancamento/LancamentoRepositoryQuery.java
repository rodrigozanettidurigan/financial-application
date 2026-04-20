package com.rzd.financial_api.domain.repository.lancamento;

import com.rzd.financial_api.domain.entity.Lancamento;
import com.rzd.financial_api.domain.repository.filter.LancamentoFilter;

import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
