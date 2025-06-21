package org.example.productcatalogservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.productcatalogservice.dto.ProductDto;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private IProductService productService;

    //object <-> json <-> string
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void Test_GetProducts_RunsSuccessfully() throws Exception {
        //Arrange
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Iphone");
        product1.setPrice(100000D);

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Macbook");

        productList.add(product1);
        productList.add(product2);
        when(productService.getAllProducts())
                .thenReturn(productList);

        //Act and Assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productList)))
                .andExpect(jsonPath("$[0].name").value("Iphone"))
                .andExpect(jsonPath("$[0].length()").value(3))
                .andExpect(jsonPath("$.length()").value(2));
    }

//    [
//            {"name":""}
//    ,
//    {"name":""}
//    ]

    @Test
    public void Test_CreateProduct_RunsSuccessfully() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setId(5);
        productDto.setName("ipad");
        productDto.setDescription("writing pad");

        Product product = new Product();
        product.setId(5);
        product.setName("ipad");
        product.setDescription("writing pad");
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products").content(objectMapper.writeValueAsString(productDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productDto)))
                .andExpect(jsonPath("$.name").value("ipad"))
                .andExpect(jsonPath("$.length()").value(3));
    }
}

//    {
//        "id" : 5,
//        "name" : "ipad",
//        "description" : "dusfdiwhfd"
//    }
