package com.pbe.db_restaurante.repository;

import com.pbe.db_restaurante.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
