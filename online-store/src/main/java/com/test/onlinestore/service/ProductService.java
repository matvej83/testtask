package com.test.onlinestore.service;

import com.test.onlinestore.entity.Product;
import com.test.onlinestore.exception.RecordNotFoundException;

import java.util.List;

public interface ProductService {

    Product getProductById(Long id);

    void createProduct(Product product);

    void updateProduct(Product product, Long id) throws RecordNotFoundException;

    void deleteProductById(Long id) throws RecordNotFoundException;

    void deleteProductByName(String name) throws RecordNotFoundException;

    List<Product> findAll();

    Product findByName(String name);

    List<Product> findAllByPriceBetween(Double minPrice, Double maxPrice);

}
