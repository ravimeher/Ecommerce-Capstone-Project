package org.example.productcatalogservice.TableInhertanceExample.TablePerClass;

import jakarta.persistence.Entity;

@Entity(name = "tpc_instructor")
public class instructor extends user{
    private String company;
}
