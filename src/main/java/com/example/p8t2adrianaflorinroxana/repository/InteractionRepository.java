package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.PublicInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<PublicInteraction, Long> {
    List<PublicInteraction> findAllByCitezenEmail(String citezenEmail);
}
