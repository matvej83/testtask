package com.test.onlinestore.controller;

import com.test.onlinestore.entity.Cart;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.CartRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"cart"})
@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private final CartRepository cartRepository;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @ApiOperation(value = "Find all carts", response = Cart.class)
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    public List<Cart> getAllCarts(Model model)
            throws RecordNotFoundException {
        model.addAttribute("method", "get");
        return cartRepository.findAll();
    }

    @ApiOperation(value = "Create a new cart", response = Cart.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public boolean addCart(Model model) {
        Cart cart = new Cart();
        cartRepository.save(cart);
        model.addAttribute("cart", cart);
        return true;
    }

    @ApiOperation(value = "Delete cart", response = Cart.class)
    @RequestMapping(value = "/deleteCart/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public boolean deleteCart(@PathVariable("id") Long id, Model model) {
        cartRepository.deleteById(id);
        model.addAttribute("method", "post");
        return true;
    }

}
