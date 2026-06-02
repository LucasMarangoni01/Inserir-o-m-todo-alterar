package com.pbe.db_sorveteria.repository;

import com.pbe.db_sorveteria.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}