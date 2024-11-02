package org.example.productcatalogservice.stubs;

import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.service.IProductService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@Primary
public class ProductServiceStub implements IProductService {
    Map<Integer, Product> productMap;

    public ProductServiceStub() {
        this.productMap = new HashMap<>();
    }


    @Override
    public Product getProductById(int id) {
        return productMap.get(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return (List<Product>)productMap.values();
    }

    @Override
    public Product createProduct(Product product) {
        productMap.put(product.getId(),product);
        return productMap.get(product.getId());
    }

    @Override
    public Product deleteProduct(int id) {
        return productMap.remove(id);
    }

    @Override
    public Product getProductBasedOnUserScope(Long pid, Long uid) {
        return null;
    }

    @Override
    public Product replaceProduct(int id, Product product) {
        productMap.put(id,product);
        return productMap.get(id);
    }
}
