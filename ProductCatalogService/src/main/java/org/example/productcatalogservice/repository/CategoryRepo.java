package org.example.productcatalogservice.repository;

import org.example.productcatalogservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Optional<Category> findById(int id);


    Optional<Category> findByName(String name);
}
