package com.pbe.db_sorveteria.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class Aluno extends Pessoa {
}

