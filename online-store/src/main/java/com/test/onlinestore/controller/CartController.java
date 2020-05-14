package com.test.onlinestore.controller;

import com.test.onlinestore.entity.Cart;
import com.test.onlinestore.entity.Product;
import com.test.onlinestore.entity.ProductsInCart;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.CartRepository;
import com.test.onlinestore.repository.ProductRepository;
import com.test.onlinestore.repository.ProductsInCartRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = {"cart"})
@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private final ProductsInCartRepository productsInCartRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @ApiOperation(value = "Create new cart", response = Cart.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public boolean addCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);
        return true;
    }

    @ApiOperation(value = "Get product in cart by id", response = ProductsInCart.class)
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = "application/json")
    public ProductsInCart getProductById(@PathVariable("id") Long id)
            throws RecordNotFoundException {
        return productsInCartRepository.findProductsInCartById(id);
    }

    @ApiOperation(value = "Get all products in carts", response = ProductsInCart.class)
    @RequestMapping(value = "/products/all", method = RequestMethod.GET, produces = "application/json")
    public List<ProductsInCart> getAllProductsInCart()
            throws RecordNotFoundException {
        return productsInCartRepository.findAll();
    }

    @ApiOperation(value = "Add new product in the cart")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public boolean addToCart(@RequestParam Long cartId, @RequestParam Long productId, @RequestParam int quantity,
                             Map<String, Object> model) {

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return false;
        }

        Cart cart = cartRepository.findCartById(cartId);
        if (cart == null) {
            return false;
        }
        ProductsInCart productsInCart = new ProductsInCart();
        productsInCart.setCart(cart);
        productsInCart.setProduct(product);
        productsInCart.setQuantity(quantity);
        productsInCartRepository.save(productsInCart);
        Iterable<ProductsInCart> productsInCarts = productsInCartRepository.findAll();
        model.put("productsInCart", productsInCarts);
        return true;
    }

    @ApiOperation(value = "Delete product in cart", response = ProductsInCart.class)
    @RequestMapping(value = "/product/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public boolean delete(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return true;
    }

}
