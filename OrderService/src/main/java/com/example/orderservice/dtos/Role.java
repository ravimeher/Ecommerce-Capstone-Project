package com.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
