package com.rzd.financial_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rzd.financial_api.entity.Categoria;
                                                                //Long é o tipo da chave primária da categoria
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
