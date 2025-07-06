package org.example.productcatalogservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Product extends BaseModel{
    private String name;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private Double price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Category category;
//    @Column(name = "is_prime_specific")
//    private Boolean isPrimeSpecific;
    @Column(name = "is_private")
    private Boolean isPrivate = false;

}
