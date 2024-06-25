package org.example.productcatalogservice.controller;

import org.example.productcatalogservice.dto.ProductDto;
import org.example.productcatalogservice.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public List<ProductDto> getProducts() {
        return new ArrayList<ProductDto>();
    }
    @GetMapping("{id}")
    public ProductDto getProductById(@PathVariable("id") int id1) {
        ProductDto  product = new ProductDto();
        product.setId(id1);
        return product;
    }
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto product) {
         return product;
    }
    @PutMapping("{id}")
    public ProductDto  replaceProduct(@PathVariable int id,@RequestBody ProductDto product) {
        return product;
    }
}
