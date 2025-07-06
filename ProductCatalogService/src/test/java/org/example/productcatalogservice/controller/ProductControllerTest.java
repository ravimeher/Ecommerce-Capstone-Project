package org.example.productcatalogservice.controller;

import org.example.productcatalogservice.dto.ProductDto;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.service.IProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private ProductController productController;
    @MockBean
    private IProductService productService;

    @Captor
    ArgumentCaptor<Long> idCaptor;

    @Test
    public void Test_getProductById_WithValidId_ReturnsProductSuccessfully() {

        //Arrange
        int id  = 1;
        Product product = new Product();
        product.setId(id);
        product.setName("Test");
        //when(productService.getProductById(any(Integer.class))).thenReturn(new Product());
        when(productService.getProductById(id))
                .thenReturn(product);

        //Act
        ResponseEntity<ProductDto> response = productController.getProductById(1);
        //Assert
        assertNotNull(response.getBody());
        assertEquals(id,response.getBody().getId());
        assertEquals("Test",response.getBody().getName());
    }

    @Test
    @DisplayName("paramter 0 resulted in product not present exception")
    public void Test_GetProductById_WithInvalidId_ThrowsException() {
        //Act and Assert
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> productController.getProductById(0));

        assertEquals("id is not valid",ex.getMessage());

        verify(productService,times(0))
                .getProductById(0);
    }

    @Test
    public void Test_GetProductById_ProductServiceThrowsException() {
        //Arrange
        when(productService.getProductById(any(Integer.class)))
                .thenThrow(new RuntimeException("something went bad"));

        //Act and Assert
        assertThrows(RuntimeException.class,
                () -> productController.getProductById(1));
    }
    @Test
    public void Test_GetProductById_CheckIfProductServiceCalledWithExpectedArguments() {
        //Arrange
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("Iphone20");

        when(productService.getProductById(any(Long.class)))
                .thenReturn(product);

        //Act
        productController.getProductById(id);

        //Assert
        verify(productService).getProductById(idCaptor.capture());
        assertEquals(id,idCaptor.getValue());
    }
@Test
    public void Test_ReplaceProducts_WithValidArguments_ReturnsProductSuccessfully() {
        //Arrange
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("Iphone20");
        Product replacedProduct = new Product();
        replacedProduct.setId(id);
        replacedProduct.setName("Iphone17");
        when(productService.replaceProduct(id,product)).thenReturn(replacedProduct);
        //Act
        product = productService.replaceProduct(id,product);
        //Assert
        assertEquals(product.getName(),replacedProduct.getName());
    }
}