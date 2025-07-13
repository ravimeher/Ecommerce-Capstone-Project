package org.example.productcatalogservice.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Role extends BaseModel {
    private String value;

    public Role(String roleName) {
        this.value = roleName;
    }

    public Role() {

    }

//    @ManyToMany(mappedBy = "roleSet")
//    private Set<User> user;
}
