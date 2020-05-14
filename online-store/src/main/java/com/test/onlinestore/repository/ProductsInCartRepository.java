package com.test.onlinestore.repository;

import com.test.onlinestore.entity.ProductsInCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsInCartRepository extends JpaRepository<ProductsInCart, Long> {

    ProductsInCart findProductsInCartById(Long id);
}
