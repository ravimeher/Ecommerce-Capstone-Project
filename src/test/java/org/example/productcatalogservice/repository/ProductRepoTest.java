package org.example.productcatalogservice.repository;

import org.example.productcatalogservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Test
    @Transactional
    public void TestJPAQueries()
    {
//        List<Product> productList = productRepo.findProductsByPriceBetween(50000D,70000D);
//        List<Product> products = productRepo.findAllByIsPrimeSpecific(true);
//        List<Product> products = productRepo.findAllByIsPrimeSpecificTrue();
//        List<Product> products = productRepo.findAllByStateActive();
//        List<Product> products = productRepo.findAllByOrderByPriceDesc();
//        String productName = productRepo.findProductNameById(1);
        Optional<Product> optionalProduct = productRepo.findById(1);
        if(optionalProduct.isPresent()) {
            System.out.println(optionalProduct.get().getName());
        }

//        String productName = productRepo.findProductNamefromId(1L);
//        String categoryName = productRepo.findCategoryNameFromProductId(2);
    }

}