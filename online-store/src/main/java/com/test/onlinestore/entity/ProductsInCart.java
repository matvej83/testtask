package com.test.onlinestore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "productsincart")
public class ProductsInCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "integer default 1")
    private int quantity;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Cart cart;

    public ProductsInCart() {
    }

    public ProductsInCart(int quantity) {
        this.quantity = quantity;
    }

}
