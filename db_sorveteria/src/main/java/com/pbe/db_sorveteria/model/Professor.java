package com.pbe.db_sorveteria.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("P")
public class Professor extends Pessoa {
}
