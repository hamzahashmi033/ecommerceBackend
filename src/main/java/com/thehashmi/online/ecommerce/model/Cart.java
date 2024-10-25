package com.thehashmi.online.ecommerce.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItems> items = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addItem(CartItems item) {
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }
    public void removeItem(CartItems item) {
        this.items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }
    private void updateTotalAmount() {
        BigDecimal newTotal = BigDecimal.ZERO;
        for (CartItems item : items) {
            BigDecimal unitPrice = item.getUnitPrice();
            if (unitPrice == null) {
                unitPrice = BigDecimal.ZERO;
            }
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            newTotal = newTotal.add(itemTotal);
        }
        this.totalAmount = newTotal;
    }

}
