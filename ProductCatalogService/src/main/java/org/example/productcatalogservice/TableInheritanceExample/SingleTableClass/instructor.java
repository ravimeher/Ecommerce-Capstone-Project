package org.example.productcatalogservice.TableInheritanceExample.SingleTableClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("instructor")
public class instructor extends user {
    private String company;
}
