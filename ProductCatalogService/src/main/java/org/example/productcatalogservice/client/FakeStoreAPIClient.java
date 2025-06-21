package org.example.productcatalogservice.client;

import org.example.productcatalogservice.dto.FakeStoreProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FakeStoreAPIClient {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public List<FakeStoreProductDto> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto[]> responseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products",FakeStoreProductDto[].class);
        FakeStoreProductDto[] fakeStoreProductDtosArray = responseEntity.getBody();
        return List.of(fakeStoreProductDtosArray);
    }

    public FakeStoreProductDto getProductById(int id){
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = requestForEntity(HttpMethod.GET,"https://fakestoreapi.com/products/{productId}",null,FakeStoreProductDto.class,id);
        if(fakeStoreProductDtoResponseEntity.getBody() != null &&
                fakeStoreProductDtoResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            return fakeStoreProductDtoResponseEntity.getBody();
        }
        return null;
    }

    public FakeStoreProductDto replaceProduct(int id, FakeStoreProductDto fakeStoreProductDtoReq){
        FakeStoreProductDto fakeStoreProductDto = requestForEntity(HttpMethod.PUT,"https://fakestoreapi.com/products/{id}",fakeStoreProductDtoReq,FakeStoreProductDto.class,id).getBody();
        return fakeStoreProductDto;
    }

    public FakeStoreProductDto deleteProduct(int id){
        FakeStoreProductDto fakeStoreProductDto = requestForEntity(HttpMethod.DELETE,"https://fakestoreapi.com/products/{id}",null,FakeStoreProductDto.class,id).getBody();
        return fakeStoreProductDto;
    }

    public <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod,String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
}
