package com.test.onlinestore.controller;

import com.test.onlinestore.entity.Cart;
import com.test.onlinestore.entity.Product;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.CartRepository;
import com.test.onlinestore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@AllArgsConstructor
public class OnlineStoreController {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @GetMapping
    public String main(Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "main";
    }

    @GetMapping("/allCarts")
    public String getCarts(Model model) {
        Iterable<Cart> carts = cartRepository.findAll();
        model.addAttribute("carts", carts);
        return "main";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam String filter, Model model) throws RecordNotFoundException {
        Product products;
        products = productRepository.findProductById(Long.valueOf(filter));
        model.addAttribute("products", products);
        return "main";
    }

}