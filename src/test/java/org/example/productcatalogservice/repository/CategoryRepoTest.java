package org.example.productcatalogservice.repository;

import org.example.productcatalogservice.model.Category;
import org.example.productcatalogservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepoTest {

    @Autowired
    private CategoryRepo categoryRepo;

    @Test
    @Transactional
    void TestingFetchTypes()
    {
        Optional<Category> optionalCategory = categoryRepo.findById(1);
        System.out.println(optionalCategory.get().getName());
        for(Product p : optionalCategory.get().getProductList()) {
            System.out.println(p.getName());
        }
    }
}