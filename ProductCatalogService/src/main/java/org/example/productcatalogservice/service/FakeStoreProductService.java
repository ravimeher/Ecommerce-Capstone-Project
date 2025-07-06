package org.example.productcatalogservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.productcatalogservice.client.FakeStoreAPIClient;
import org.example.productcatalogservice.dto.FakeStoreProductDto;
import org.example.productcatalogservice.model.Category;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@Primary
public class FakeStoreProductService implements IProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private FakeStoreAPIClient fakeStoreAPIClient;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public FakeStoreProductService(RestTemplateBuilder restTemplateBuilder,FakeStoreAPIClient fakeStoreAPIClient,RedisTemplate<String,Object> redisTemplate) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.fakeStoreAPIClient = fakeStoreAPIClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductById(long id) {
        FakeStoreProductDto fakeStoreProductDto = null;
//        fakeStoreProductDto = (FakeStoreProductDto) redisTemplate.opsForHash()
//                .get("_PRODUCTS",id);
//        if(fakeStoreProductDto != null) {
//            System.out.println("Found in Cache");
//            return  from(fakeStoreProductDto);
//        }
        fakeStoreProductDto = fakeStoreAPIClient.getProductById(id);
        System.out.println("Found by calling fakestore");
        //redisTemplate.opsForHash().put("_PRODUCTS",id,fakeStoreProductDto);
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
    public Product deleteProduct(long id) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreAPIClient.deleteProduct(id);
        return from(fakeStoreProductDto);
    }

    @Override
    public Product getProductBasedOnUserScope(Long pid, Long uid) {
        return null;
    }

    @Override
    public List<Product> populateFromFakeStore(int count) {
        return List.of();
    }

    public List<Product> sendNumberOfProducts(int count) {
        List<FakeStoreProductDto> fakeStoreProductDtos = fakeStoreAPIClient.getAllProducts();
        List<Product> products = new ArrayList<>();
        for(int i=0;i<count;i++){
           products.add(from(fakeStoreProductDtos.get(i)));
        }
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product replaceProduct(long id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreAPIClient.replaceProduct(id,from(product));
        return from(fakeStoreProductDto);
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        //product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
//        Category category = new Category();
//        //category.setId(product.getId());
//        category.setName(fakeStoreProductDto.getCategory());
        Category category = categoryRepo.findByName(fakeStoreProductDto.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(fakeStoreProductDto.getCategory());
                    return categoryRepo.save(newCategory);
                });
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
