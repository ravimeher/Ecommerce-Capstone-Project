package org.example.productcatalogservice.TableInhertanceExample.SingleTableClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "stc_ta")
@DiscriminatorValue("ta")
public class ta extends user {
    private Double ratings;
}
