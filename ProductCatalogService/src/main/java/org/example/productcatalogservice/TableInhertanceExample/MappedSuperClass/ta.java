package org.example.productcatalogservice.TableInhertanceExample.MappedSuperClass;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "mpc_ta")
public class ta extends user {
    private Double ratings;
}
