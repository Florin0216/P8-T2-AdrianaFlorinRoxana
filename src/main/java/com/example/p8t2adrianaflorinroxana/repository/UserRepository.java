package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
