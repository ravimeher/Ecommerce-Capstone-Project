package org.example.productcatalogservice.service;

import org.example.productcatalogservice.dto.SortParam;
import org.example.productcatalogservice.model.Product;
import org.example.productcatalogservice.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private ProductRepo productRepo;

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
