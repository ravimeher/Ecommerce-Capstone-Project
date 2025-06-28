package org.example.productcatalogservice.TableInheritanceExample.MappedSuperClass;

import jakarta.persistence.Entity;

@Entity(name = "mpc_ta")
public class ta extends user {
    private Double ratings;
}
