package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.Stations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Stations, Long> {
    Stations findStationsById(long id);
}
