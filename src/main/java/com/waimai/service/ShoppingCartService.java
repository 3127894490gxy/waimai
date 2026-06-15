package com.waimai.service;

import com.waimai.entity.ShoppingCart;
import com.waimai.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepository;

    public ShoppingCartService(ShoppingCartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public ShoppingCart addOrUpdate(ShoppingCart cart) {
        Optional<ShoppingCart> existing = cartRepository.findByUserIdAndDishId(cart.getUserId(), cart.getDishId());
        if (existing.isPresent()) {
            ShoppingCart item = existing.get();
            item.setQuantity(item.getQuantity() + cart.getQuantity());
            return cartRepository.save(item);
        }
        return cartRepository.save(cart);
    }

    public List<ShoppingCart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public void clearByUserId(Long userId) {
        cartRepository.deleteByUserId(userId);
    }

    public void removeItem(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }
}
