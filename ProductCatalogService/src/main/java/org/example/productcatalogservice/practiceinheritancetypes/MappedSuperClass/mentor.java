package org.example.productcatalogservice.practiceinheritancetypes.MappedSuperClass;

import jakarta.persistence.Entity;

@Entity(name = "mpc_mentor")
public class mentor extends user {
    private Long hours;
}
