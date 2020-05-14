package com.test.onlinestore.repository;

import com.test.onlinestore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    Product findProductById(Long id);

    List<Product> findAllByPriceBetween(Double minPrice, Double maxPrice);

    void deleteByName(String name);
}