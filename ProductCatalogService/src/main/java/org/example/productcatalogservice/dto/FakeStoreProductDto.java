package org.example.productcatalogservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FakeStoreProductDto {
    private int id;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private String category;
    private FakeStoreRatingDto fakeStoreRatingDto;
}
