package com.pbe.db_sorveteria.repository;

import com.pbe.db_sorveteria.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
