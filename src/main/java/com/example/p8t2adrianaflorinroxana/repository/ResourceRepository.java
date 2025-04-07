package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resources, Long> {

    List<Resources> findByAgentId(long agentId);
    List<Resources> findByStationId(long stationId);
    List<Resources> findByMaintenanceDateNotNull();
    List<Resources> findByMaintenanceDateBefore(LocalDate date);
    List<Resources> findByMaintenanceDateNotNullAndMaintenanceDateBefore(LocalDate today);

}