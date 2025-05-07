package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agents, Long> {
    List<Agents> findByStation_Id(long stationId);

    void deleteById(long id);

    List<Agents> findAllByFirstNameOrLastName(String firstName, String lastName);

    Agents findByEmail(String email);
}
