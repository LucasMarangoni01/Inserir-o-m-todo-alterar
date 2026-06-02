package com.pbe.db_lanchonete.repository;

import com.pbe.db_lanchonete.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}