package org.example.productcatalogservice.service;

import org.example.productcatalogservice.dto.SortParam;
import org.example.productcatalogservice.dto.UserDto;
import org.example.productcatalogservice.exceptions.*;
import org.example.productcatalogservice.model.Category;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.model.Role;
import org.example.productcatalogservice.repository.CategoryRepo;
import org.example.productcatalogservice.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private FakeStoreProductService fakeStoreProductService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Product getProductById(long id) {
        Optional<Product> product = productRepo.findById(id);
        if(!product.isPresent()){
            throw new ProductNotFoundException("No Product for the id");
        }
        return  product.get();
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products;
    }

    @Override
    public Product replaceProduct(long id, Product product) {
        Product oldProduct = getProductById(id);
        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setQuantityAvailable(product.getQuantityAvailable());
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
    public Product deleteProduct(long id) {
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

        UserDto user = restTemplate.getForEntity("http://userservice/user/{uid}", UserDto.class,uid).getBody();
        System.out.println("Call was made to user service");
        if(user != null && user.getRoles().contains(new Role("ADMIN")))
            return product.get();

        return null;
    }

    @Override
    public List<Product> populateFromFakeStore(int count) {
        List<Product> productsList =  fakeStoreProductService.sendNumberOfProducts(count);
        for(Product product : productsList){
            productRepo.save(product);
        }
        return productsList;
    }

    @Override
    public List<Product> getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()-> new CategoryNotFoundException("Not Valid Category"));
        return productRepo.findAllByCategoryId(categoryId);
    }
    @Override
    public Page<Product> searchProducts(String query, Integer pageNumber, Integer pageSize, List<SortParam> sortParamList){
        //Sort sort = Sort.by("price").descending();
        Sort sort=null;
        if(!sortParamList.isEmpty()){
            if(sortParamList.get(0).getSortType().equals("ASC")){
                sort = Sort.by(sortParamList.get(0).getParamName());
            }
            else
                sort = Sort.by(sortParamList.get(0).getParamName()).descending();
        }
        for(int i=1;i<sortParamList.size();i++){
            if(sortParamList.get(i).getSortType().equals("ASC")){
                sort.and(Sort.by(sortParamList.get(i).getParamName()));
            }
            else
                sort.and(Sort.by(sortParamList.get(i).getParamName()).descending());
        }
        Page<Product> p = productRepo.findProductsByName(query, PageRequest.of(pageNumber,pageSize,sort));
        return p;
    }
}
