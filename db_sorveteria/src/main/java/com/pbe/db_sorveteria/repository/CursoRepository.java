package com.pbe.db_sorveteria.repository;

import com.pbe.db_sorveteria.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}