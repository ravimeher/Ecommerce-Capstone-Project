package org.example.productcatalogservice.repository;

import org.example.productcatalogservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    Page<Product> findProductsByName(String query, Pageable pageable);
    Optional<Product> findById(Long id);

    //Testing Jpa Queries
    //If JPA is not able to create query project won't start
    //To test go here - org/example/productcatalogservice/repository/ProductRepoTest.java
    List<Product> findProductsByPriceBetween(Double low,Double high);
   // List<Product> findAllByIsPrimeSpecific(boolean prime);
    //List<Product> findAllByIsPrimeSpecificTrue();
//    List<Product> findAllByStateActive();
    List<Product> findAllByOrderByPriceDesc();

    @Query("select p.name from Product p where p.id =?1")//positional arguments where id is used by ?1
    String findProductNameById(int id);

    @Query("select c.name from Product p join Category c on c.id = p.category.id where p.id=:productid")//named arguments using id:productId
    String findCategoryNameFromProductId(int productid);

    List<Product> findAllByCategoryId(Long categoryId);
}
