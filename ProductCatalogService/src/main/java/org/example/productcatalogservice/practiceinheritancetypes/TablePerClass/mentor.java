package org.example.productcatalogservice.practiceinheritancetypes.TablePerClass;

import jakarta.persistence.Entity;

@Entity(name = "tpc_mentor")
public class mentor extends user{
    private Long hours;
}
