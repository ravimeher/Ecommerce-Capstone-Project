package org.example.productcatalogservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseModel {
    private int id;
    private Date CreatedAt;
    private Date UpdatedAt;
    private State state;
}
