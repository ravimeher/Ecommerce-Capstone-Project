package org.example.productcatalogservice.repository;

import org.example.productcatalogservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    Optional<Product> findById(Long id);
    List<Product> findProductsByPriceBetween(Double low,Double high);
    List<Product> findAllByIsPrimeSpecific(boolean prime);
    List<Product> findAllByIsPrimeSpecificTrue();
//    List<Product> findAllByStateActive();
    List<Product> findAllByOrderByPriceDesc();

    @Query("select p.name from Product p where p.id =?1")
    String findProductNameById(int id);

    @Query("select c.name from Product p join Category c on c.id = p.category.id where p.id=:productid")
    String findCategoryNameFromProductId(int productid);
}
