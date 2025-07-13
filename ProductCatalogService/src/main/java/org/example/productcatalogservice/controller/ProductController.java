package org.example.productcatalogservice.controller;

import org.example.productcatalogservice.client.UserServiceClient;
import org.example.productcatalogservice.dto.CategoryDto;
import org.example.productcatalogservice.dto.ProductDto;
import org.example.productcatalogservice.dto.SearchRequestDto;
import org.example.productcatalogservice.dto.UserDto;
import org.example.productcatalogservice.exceptions.UserNotAuthenticatedException;
import org.example.productcatalogservice.exceptions.UserNotAuthorised;
import org.example.productcatalogservice.model.Category;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/{pid}/{uid}")
    public ResponseEntity<ProductDto> getProductBasedOnUserScope(@PathVariable Long pid,@PathVariable Long uid){
        Product product = productService.getProductBasedOnUserScope(pid,uid);
        if(product == null)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(from(product),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = from(product);
            productDtos.add(productDto);
        }
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<Product> products = productService.getAllProductsByCategory(categoryId);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = from(product);
            productDtos.add(productDto);
        }
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") long id) {
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
            return new ResponseEntity<>(productDto,headers,HttpStatus.OK);
        }catch (IllegalArgumentException exception){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            //throw exception;
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product,@RequestHeader("Authorization") String token) {
        UserDto user = userServiceClient.validateToken(token);
        if(user == null){
            throw new UserNotAuthenticatedException("Token is not valid");
        }
        if(!userServiceClient.hasRequiredRole(user, "ADMIN")){
            throw new UserNotAuthorised("You do not have the required role");
        }
        Product product1 = from(product);
        Product response = productService.createProduct(product1);
        return new ResponseEntity<>(from(response),HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto>  replaceProduct(@PathVariable long id,@RequestBody ProductDto productDto,@RequestHeader("Authorization") String token) {
        UserDto user = userServiceClient.validateToken(token);
        if(user == null){
            throw new UserNotAuthenticatedException("Token is not valid");
        }
        if(!userServiceClient.hasRequiredRole(user, "ADMIN")){
            throw new UserNotAuthorised("You do not have the required role");
        }
        Product product = from(productDto);
        Product result = productService.replaceProduct(id,product);
        return new ResponseEntity<>(from(result),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable long id,@RequestHeader("Authorization") String token) {
        UserDto user = userServiceClient.validateToken(token);
        if(user == null){
            throw new UserNotAuthenticatedException("Token is not valid");
        }
        if(!userServiceClient.hasRequiredRole(user, "ADMIN")){
            throw new UserNotAuthorised("You do not have the required role");
        }
        Product product = productService.deleteProduct(id);
        return new ResponseEntity<>(from(product),HttpStatus.OK);
    }

    @GetMapping("fill/{count}")
    public ResponseEntity<List<ProductDto>> populateProductsFromFakeStore(@PathVariable int count){
        List<Product> products = productService.populateFromFakeStore(count);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(from(product));
        }
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

    @PostMapping("/search")
    public Page<Product> searchProducts(@RequestBody SearchRequestDto searchRequestDto){
        Page<Product> productList =  productService.searchProducts(searchRequestDto.getQuery(),
                searchRequestDto.getPageNumber(), searchRequestDto.getPageSize(),searchRequestDto.getSortParamList());
        return productList;
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
        //productDto.setIsPrivate(product.getIsPrivate());
        return productDto;
    }

}
