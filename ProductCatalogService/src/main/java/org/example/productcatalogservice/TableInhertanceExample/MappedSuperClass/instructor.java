package org.example.productcatalogservice.TableInhertanceExample.MappedSuperClass;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "mpc_instructor")
public class instructor extends user {
    private String company;
}
