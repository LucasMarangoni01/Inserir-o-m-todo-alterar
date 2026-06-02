package com.pbe.db_sorveteria.repository;

import com.pbe.db_sorveteria.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
