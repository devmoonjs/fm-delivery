package com.sparta.fmdelivery.domain.ad.repository;

import com.sparta.fmdelivery.domain.ad.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ads, Long> {
}
