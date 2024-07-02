package org.example.productcatalogservice.service;

import org.example.productcatalogservice.client.FakeStoreAPIClient;
import org.example.productcatalogservice.dto.FakeStoreProductDto;
import org.example.productcatalogservice.model.Category;
import org.example.productcatalogservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements IProductService {
    @Override
    public Product deleteProduct(int id) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreAPIClient.deleteProduct(id);
        return from(fakeStoreProductDto);
    }

    @Autowired
    private FakeStoreAPIClient fakeStoreAPIClient;

    @Override
    public Product getProductById(int id) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreAPIClient.getProductById(id);
        return from(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        List<FakeStoreProductDto> fakeStoreProductDtos = fakeStoreAPIClient.getAllProducts();
        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos){
            Product product = from(fakeStoreProductDto);
            products.add(product);
        }
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product replaceProduct(int id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreAPIClient.replaceProduct(id,from(product));
        return from(fakeStoreProductDto);
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        Category category = new Category();
        category.setId(product.getId());
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        product.setImageUrl(fakeStoreProductDto.getImageUrl());
        return product;
    }
    private FakeStoreProductDto from(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImageUrl(product.getImageUrl());
        fakeStoreProductDto.setTitle(product.getName());
        if(product.getCategory() != null) {
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        fakeStoreProductDto.setDescription(product.getDescription());

        return fakeStoreProductDto;
    }
}
