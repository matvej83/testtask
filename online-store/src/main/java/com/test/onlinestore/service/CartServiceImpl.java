package com.test.onlinestore.service;

import com.test.onlinestore.entity.Cart;
import com.test.onlinestore.entity.Product;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.CartRepository;
import com.test.onlinestore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findCartById(id);
    }

    @Override
    public void createCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void updateCart(Cart cart, Long id) throws RecordNotFoundException {
        cartRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No cart record exist for given id!"));
        cartRepository.save(cart);
    }

    @Override
    public List<Product> getProductInCart() {
        return productRepository.findAll();
    }
}
