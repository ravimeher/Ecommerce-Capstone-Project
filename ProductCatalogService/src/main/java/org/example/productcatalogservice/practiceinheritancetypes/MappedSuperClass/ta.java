package org.example.productcatalogservice.practiceinheritancetypes.MappedSuperClass;

import jakarta.persistence.Entity;

@Entity(name = "mpc_ta")
public class ta extends user {
    private Double ratings;
}
