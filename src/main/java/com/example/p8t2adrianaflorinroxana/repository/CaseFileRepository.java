package com.example.p8t2adrianaflorinroxana.repository;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.CaseFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface CaseFileRepository extends JpaRepository<CaseFiles, Long> {
    List<CaseFiles> findAllByCaseCategory(String caseCategory);

    List<CaseFiles> findAllByCaseName(String caseName);

    List<CaseFiles> findAllByAgents(List<Agents> agents);

    List<CaseFiles> findAllByCaseNameOrAgents(String caseName, List<Agents> agents);

    List<CaseFiles> findAllByStatus(String status);

    List<CaseFiles> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    long countByCreatedAtBetween(LocalDateTime prevYearStart, LocalDateTime prevYearEnd);
}
