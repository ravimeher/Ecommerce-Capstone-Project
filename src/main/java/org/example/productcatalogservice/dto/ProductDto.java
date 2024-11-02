package org.example.productcatalogservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.productcatalogservice.model.Category;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private int id;
    private String name;
    private String description;
    private Double price;
    private CategoryDto category;
    private String imageUrl;
    private Boolean isPrivate;
}
