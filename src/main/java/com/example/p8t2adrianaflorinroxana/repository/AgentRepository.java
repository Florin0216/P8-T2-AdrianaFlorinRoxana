package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agents, Integer> {
    List<Agents> findByStation_Id(long stationId);

    void deleteById(long id);

    Agents findByUser(Users user);
}
