package org.example.productcatalogservice.TableInhertanceExample.SingleTableClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "stc_instructor")
@DiscriminatorValue("instructor")
public class instructor extends user {
    private String company;
}
