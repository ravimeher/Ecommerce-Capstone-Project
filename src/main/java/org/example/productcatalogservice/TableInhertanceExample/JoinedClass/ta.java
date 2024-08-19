package org.example.productcatalogservice.TableInhertanceExample.JoinedClass;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name = "jc_ta")
@PrimaryKeyJoinColumn(name="user_id")
public class ta extends user {
    private Double ratings;
}
