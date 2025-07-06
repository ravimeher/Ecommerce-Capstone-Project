package org.example.productcatalogservice.service;

import org.example.productcatalogservice.model.Product;

import java.util.List;

public interface IProductService {
    Product getProductById(long id);

    List<Product> getAllProducts();

    Product replaceProduct(long id, Product product);
    Product createProduct(Product product);
    Product deleteProduct(long id);

    Product getProductBasedOnUserScope(Long pid, Long uid);
    List<Product> populateFromFakeStore(int count);
}
