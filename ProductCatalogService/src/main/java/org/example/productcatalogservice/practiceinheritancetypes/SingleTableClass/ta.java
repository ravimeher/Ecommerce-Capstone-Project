package org.example.productcatalogservice.practiceinheritancetypes.SingleTableClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ta")
public class ta extends user {
    private Double ratings;
}
