package com.pbe.db_sorveteria.repository;

import com.pbe.db_sorveteria.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
