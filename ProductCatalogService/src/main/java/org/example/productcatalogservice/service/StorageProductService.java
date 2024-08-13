package org.example.productcatalogservice.service;

import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class StorageProductService implements IProductService {
    @Autowired
    private ProductRepo productRepo;

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
        Product newProduct = productRepo.save(product);
        return newProduct;
    }

    @Override
    public Product deleteProduct(int id) {
        Product product = getProductById(id);
        product.setCategory(null);
        productRepo.delete(product);
        return product;
    }
}
