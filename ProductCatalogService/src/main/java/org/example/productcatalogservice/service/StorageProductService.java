package org.example.productcatalogservice.service;

import org.example.productcatalogservice.dto.UserDto;
import org.example.productcatalogservice.model.Category;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.repository.CategoryRepo;
import org.example.productcatalogservice.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class StorageProductService implements IProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Product getProductById(int id) {
        Optional<Product> product = productRepo.findById(id);
        if(product.isPresent()){
            return product.get();
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products;
    }

    @Override
    public Product replaceProduct(int id, Product product) {
        Product oldProduct = getProductById(id);
        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategory(product.getCategory());
        productRepo.save(oldProduct);
        return oldProduct;
    }

    @Override
    public Product createProduct(Product product) {
        Category category = product.getCategory();
        Optional<Category> presentCategory = categoryRepo.findByName(category.getName());
        if(presentCategory.isPresent()){
            product.setCategory(presentCategory.get());
        }
        {
            category = categoryRepo.save(product.getCategory());
        }

        product.setCategory(category);
        return productRepo.save(product);
    }

    @Override
    public Product deleteProduct(int id) {
        Product product = getProductById(id);
        product.setCategory(null);
        productRepo.delete(product);
        return product;
    }

    @Override
    public Product getProductBasedOnUserScope(Long pid, Long uid) {
        //Get product and see if the product is public then return product else call user service to get role
        Optional<Product> product = productRepo.findById(pid);
        if(!product.isPresent()){
            return null;
        }
        if(!product.get().getIsPrivate()){
            return product.get();
        }
        //product is private - if role is admin or seller send back product or else send null
        UserDto user = restTemplate.getForEntity("http://userservice/users/{uid}", UserDto.class,uid).getBody();
        System.out.println("Call was made to user service");
        System.out.println(user.getEmail());
        return product.get();
    }
}
