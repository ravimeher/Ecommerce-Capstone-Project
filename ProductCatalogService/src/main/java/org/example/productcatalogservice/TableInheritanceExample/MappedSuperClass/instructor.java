package org.example.productcatalogservice.TableInheritanceExample.MappedSuperClass;

import jakarta.persistence.Entity;

@Entity(name = "mpc_instructor")
public class instructor extends user {
    private String company;
}
