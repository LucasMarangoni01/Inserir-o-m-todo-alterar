package com.pbe.db_mercado.repository;

import com.pbe.db_mercado.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
