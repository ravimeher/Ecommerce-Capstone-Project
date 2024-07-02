package org.example.productcatalogservice.service;

import org.example.productcatalogservice.model.Product;

import java.util.List;

public interface IProductService {
    Product getProductById(int id);

    List<Product> getAllProducts();

    Product replaceProduct(int id, Product product);
    Product createProduct(Product product);
    Product deleteProduct(int id);
}
