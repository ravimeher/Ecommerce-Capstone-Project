package org.example.productcatalogservice.TableInheritanceExample.JoinedClass;

import jakarta.persistence.*;

@Entity(name = "jc_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class user {
    @Id
    private long id;
    private String name;
    private String email;
}
