package org.example.productcatalogservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends BaseModel{
    private String name;
    private String description;
    private Double price;
    private Category category;
    private String imageUrl;

}
