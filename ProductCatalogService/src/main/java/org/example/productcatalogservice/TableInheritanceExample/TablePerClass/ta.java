package org.example.productcatalogservice.TableInheritanceExample.TablePerClass;

import jakarta.persistence.Entity;

@Entity(name = "tpc_ta")
public class ta extends user{
    private Double ratings;
}
