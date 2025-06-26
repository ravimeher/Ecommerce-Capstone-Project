package org.example.productcatalogservice.controller;

import org.example.productcatalogservice.dto.CategoryDto;
import org.example.productcatalogservice.dto.ProductDto;
import org.example.productcatalogservice.model.Category;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/{pid}/{uid}")
    public ProductDto getProductBasedOnUserScope(@PathVariable Long pid,@PathVariable Long uid){
        Product product = productService.getProductBasedOnUserScope(pid,uid);
        return from(product);
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = from(product);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") int id) {
        try {
            if(id<1 || id >20){
                throw new IllegalArgumentException("id is not valid");
            }
            MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
            headers.add("Custom Addition", "By Ravi");
            Product product = productService.getProductById(id);
            if(product == null) {
                return new ResponseEntity<>(null,headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            ProductDto productDto = from(product);
            return new ResponseEntity<ProductDto>(productDto,headers,HttpStatus.OK);
        }catch (IllegalArgumentException exception){
            //return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            throw exception;
        }
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto product) {
        Product product1 = from(product);
        Product response = productService.createProduct(product1);
        return from(response);
    }

    @PutMapping("{id}")
    public ProductDto  replaceProduct(@PathVariable int id,@RequestBody ProductDto productDto) {
        Product product = from(productDto);
        Product result = productService.replaceProduct(id,product);
        return from(result);
    }

    @DeleteMapping("{id}")
    public ProductDto deleteProduct(@PathVariable int id) {
        Product product = productService.deleteProduct(id);
        return from(product);
    }

    private Product from(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrl());
        if(productDto.getCategory() != null){
            Category category = new Category();
            category.setId(productDto.getCategory().getId());
            category.setName(productDto.getCategory().getName());
            category.setDescription(productDto.getCategory().getDescription());
            product.setCategory(category);
        }
        else
            product.setCategory(null);
        return product;
    }
    private ProductDto from(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setPrice(product.getPrice());
        if(product.getCategory() != null)
        {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            productDto.setCategory(categoryDto);
        }
        else
            productDto.setCategory(null);
        productDto.setIsPrivate(product.getIsPrivate());
        return productDto;
    }

}
