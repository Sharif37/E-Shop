package com.sharif.eshop.repository;

import com.sharif.eshop.model.Order;
import com.sharif.eshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByEmail(String email);

}
