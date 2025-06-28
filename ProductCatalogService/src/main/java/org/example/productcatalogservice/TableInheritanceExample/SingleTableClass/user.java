package org.example.productcatalogservice.TableInheritanceExample.SingleTableClass;

import jakarta.persistence.*;

@Entity(name = "stc_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class user {
    @Id
    private long id;
    private String name;
    private String email;
}
