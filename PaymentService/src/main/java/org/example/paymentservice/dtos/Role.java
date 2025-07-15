package org.example.paymentservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {
    private String value;

    public Role(String roleName) {
        this.value = roleName;
    }

    public Role() {

    }
}
