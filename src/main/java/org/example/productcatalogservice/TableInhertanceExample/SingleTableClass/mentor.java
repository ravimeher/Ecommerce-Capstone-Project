package org.example.productcatalogservice.TableInhertanceExample.SingleTableClass;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "stc_mentor")
@DiscriminatorValue("mentor")
public class mentor extends user {
    private Long hours;
}
