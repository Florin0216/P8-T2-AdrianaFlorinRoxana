package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.*;
import com.example.p8t2adrianaflorinroxana.repository.CaseFileRepository;
import com.example.p8t2adrianaflorinroxana.repository.CaseVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaseFileServiceImpl {

    private final CaseFileRepository caseFileRepository;
    private final FileStorageServiceImpl fileStorageService;
    private final CaseVersionRepository caseVersionRepository;

    public CaseFileServiceImpl(CaseFileRepository caseFileRepository, FileStorageServiceImpl fileStorageService, CaseVersionRepository caseVersionRepository) {
        this.caseFileRepository = caseFileRepository;
        this.fileStorageService = fileStorageService;
        this.caseVersionRepository = caseVersionRepository;
    }

    public void createCaseFile(CaseFiles caseFile, MultipartFile file, String evidenceName) throws IOException {
        caseFile.setCreatedAt(LocalDateTime.now());
        caseFile.setStatus("ACTIVE");

        if (file != null) {
            fileStorageService.saveFile(file);

            CaseEvidences evidence = new CaseEvidences();
            evidence.setEvidenceName(evidenceName != null ? evidenceName : file.getOriginalFilename());
            evidence.setEvidencePath(file.getOriginalFilename());
            evidence.setCaseFile(caseFile);

            List<CaseEvidences> evidences = new ArrayList<>();
            evidences.add(evidence);
            caseFile.setCaseEvidences(evidences);
        }

        caseFileRepository.save(caseFile);
    }


    public List<CaseFiles> getAllCaseFiles() {
         return caseFileRepository.findAll();
    }

    public List<CaseFiles> getCaseFilesByNameOrAgentName(String fileName, List<Agents> agent) {

        return caseFileRepository.findAllByCaseNameOrAgents(fileName, agent);
    }

    public CaseFiles getCaseFileById(Long id) {

        return caseFileRepository.findById(id).orElse(null);
    }

    public void updateCaseFile(CaseFiles caseFiles, Users user) {
        CaseFiles oldCaseFile = getCaseFileById(caseFiles.getId());

        String versionData = "Name: " + oldCaseFile.getCaseName() + "\n"
                + "Category: " + oldCaseFile.getCaseCategory() + "\n"
                + "Description: " + oldCaseFile.getCaseDescription() + "\n"
                + "Station: " + (oldCaseFile.getStation() != null ? oldCaseFile.getStation().getStationName() : "None") + "\n"
                + "Agents: " + (oldCaseFile.getAgents() != null ? oldCaseFile.getAgents().stream()
                .map(agent -> agent.getFirstName() + " " + agent.getLastName())
                .collect(Collectors.joining(", ")) : "None");

        int nextVersion = oldCaseFile.getVersions() != null ? oldCaseFile.getVersions().size() + 1 : 1;

        CaseVersion version = new CaseVersion();
        version.setCaseFile(oldCaseFile);
        version.setVersionData(versionData);
        version.setVersionNumber(nextVersion);
        caseVersionRepository.save(version);

        oldCaseFile.setCaseName(caseFiles.getCaseName());
        oldCaseFile.setAgents(caseFiles.getAgents());
        oldCaseFile.setUpdatedAt(LocalDateTime.now());
        oldCaseFile.setStation(caseFiles.getStation());
        oldCaseFile.setCaseCategory(caseFiles.getCaseCategory());
        oldCaseFile.setCaseDescription(caseFiles.getCaseDescription());
        oldCaseFile.setLastUserAccess(user);

        caseFileRepository.save(oldCaseFile);
    }

    public CaseFiles findCaseFileById(Long id) {
        return caseFileRepository.findById(id).orElse(null);
    }

    public void addNoteToCase(Long caseId, String content, Users createdBy) {
        CaseFiles caseFile = findCaseFileById(caseId);

        CaseNotes note = new CaseNotes();
        note.setCaseFile(caseFile);
        note.setContent(content);
        note.setCreatedBy(createdBy);

        caseFile.getNotes().add(note);
        caseFileRepository.save(caseFile);
    }
}
