package org.example.userauthenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
