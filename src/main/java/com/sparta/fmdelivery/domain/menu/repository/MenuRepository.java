package com.sparta.fmdelivery.domain.menu.repository;

import com.sparta.fmdelivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByShopId(Long shopId);
}
