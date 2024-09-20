package com.sparta.fmdelivery.domain.user.repository;

import com.sparta.fmdelivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email); // 주어진 이메일을 가진 사용자가 데이터베이스에 존재하는지 확인

    Optional<User> findByEmail(String email);
}
