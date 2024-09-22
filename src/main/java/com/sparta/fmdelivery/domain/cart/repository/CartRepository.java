package com.sparta.fmdelivery.domain.cart.repository;

import com.sparta.fmdelivery.domain.cart.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Long> {
}
