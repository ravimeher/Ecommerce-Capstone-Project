package org.example.productcatalogservice.TableInhertanceExample.MappedSuperClass;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "mpc_mentor")
public class mentor extends user {
    private Long hours;
}
