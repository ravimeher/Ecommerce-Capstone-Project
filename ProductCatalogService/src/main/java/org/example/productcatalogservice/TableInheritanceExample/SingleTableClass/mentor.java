package org.example.productcatalogservice.TableInheritanceExample.SingleTableClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("mentor")
public class mentor extends user {
    private Long hours;
}
