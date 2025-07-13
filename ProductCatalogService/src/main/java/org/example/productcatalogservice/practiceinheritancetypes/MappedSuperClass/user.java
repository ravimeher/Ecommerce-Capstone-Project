package org.example.productcatalogservice.practiceinheritancetypes.MappedSuperClass;

import jakarta.persistence.*;


@MappedSuperclass
public abstract class user {
    @Id
    private long id;
    private String name;
    private String email;
}
