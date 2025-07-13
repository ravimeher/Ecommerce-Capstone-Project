//package org.example.productcatalogservice.controller;
//
//import org.example.productcatalogservice.dto.ProductDto;
//import org.example.productcatalogservice.service.IProductService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class ProductControllerFlow {
//
//    @Autowired
//    private ProductController productController;
//
//    @Autowired
//    private IProductService productService;
//
//    @Test
//    public void Test_Create_Replace_GetProduct_RunsSuccessfully() {
//        //Arrange
//        ProductDto productDto = new ProductDto();
//        productDto.setName("Iphone15");
//        productDto.setId(10);
//
//        //Act
//        ProductDto response = productController.createProduct(productDto).getBody();
//        ResponseEntity<ProductDto>  responseEntity = productController.getProductById(response.getId());
//        productDto.setName("Iphone20");
//        ProductDto response2  = productController.replaceProduct(response.getId(),productDto).getBody();
//        ResponseEntity<ProductDto>  responseEntity2 = productController.getProductById(response2.getId());
//
//
//        //Assert
//        assertEquals("Iphone15",responseEntity.getBody().getName());
//        assertEquals("Iphone20",responseEntity2.getBody().getName());
//    }
//}
