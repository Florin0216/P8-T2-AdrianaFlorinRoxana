package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.*;
import com.example.p8t2adrianaflorinroxana.repository.CaseFileRepository;
import com.example.p8t2adrianaflorinroxana.repository.CaseVersionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseFileServiceImplTest {

    @Mock
    private CaseFileRepository caseFileRepository;

    @Mock
    private FileStorageServiceImpl fileStorageService;

    @Mock
    private CaseVersionRepository caseVersionRepository;

    @InjectMocks
    private CaseFileServiceImpl caseFileService;

    @Mock
    private MultipartFile file;

    @Test
    void createCaseFile() throws IOException {
        CaseFiles caseFile = new CaseFiles();
        caseFile.setCaseName("Sample Case");

        when(file.getOriginalFilename()).thenReturn("evidence.jpg");

        caseFileService.createCaseFile(caseFile, file, "Fingerprint");

        assertEquals("ACTIVE", caseFile.getStatus());
        assertNotNull(caseFile.getCreatedAt());
        assertNotNull(caseFile.getCaseEvidences());
        assertEquals("Fingerprint", caseFile.getCaseEvidences().get(0).getEvidenceName());
        assertEquals("evidence.jpg", caseFile.getCaseEvidences().get(0).getEvidencePath());

        verify(fileStorageService).saveFile(file);
        verify(caseFileRepository).save(caseFile);
    }

    @Test
    void getCaseFilesByNameOrAgentName() {
        String caseName = "Operation Thunder";
        Agents agent = new Agents();
        List<Agents> agents = List.of(agent);
        List<CaseFiles> expected = List.of(new CaseFiles());

        when(caseFileRepository.findAllByCaseNameOrAgents(caseName, agents)).thenReturn(expected);

        List<CaseFiles> result = caseFileService.getCaseFilesByNameOrAgentName(caseName, agents);

        assertEquals(expected, result);
    }

    @Test
    void addNoteToCase() {
        Long caseId = 1L;
        String content = "This is a note.";
        Users user = new Users();

        CaseFiles caseFile = new CaseFiles();
        caseFile.setNotes(new ArrayList<>());

        when(caseFileRepository.findById(caseId)).thenReturn(Optional.of(caseFile));

        caseFileService.addNoteToCase(caseId, content, user);

        assertEquals(1, caseFile.getNotes().size());
        assertEquals(content, caseFile.getNotes().get(0).getContent());
        assertEquals(user, caseFile.getNotes().get(0).getCreatedBy());

        verify(caseFileRepository).save(caseFile);
    }

    @Test
    void updateCaseFile() {
        Long caseId = 1L;

        CaseFiles oldCase = new CaseFiles();
        oldCase.setId(caseId);
        oldCase.setCaseName("Old Case");
        oldCase.setCaseCategory("Robbery");
        oldCase.setCaseDescription("Old description");
        Stations station = new Stations();
        station.setStationName("Station A");
        oldCase.setStation(station);

        Agents agent = new Agents();
        agent.setFirstName("John");
        agent.setLastName("Doe");
        List<Agents> oldAgents = List.of(agent);
        oldCase.setAgents(oldAgents);

        oldCase.setVersions(new ArrayList<>());

        CaseFiles updatedCase = new CaseFiles();
        updatedCase.setId(caseId);
        updatedCase.setCaseName("Updated Case");
        updatedCase.setCaseCategory("Fraud");
        updatedCase.setCaseDescription("Updated description");
        Stations updatedStation = new Stations();
        updatedStation.setStationName("Station B");
        updatedCase.setStation(updatedStation);
        Agents newAgent = new Agents();
        newAgent.setFirstName("Jane");
        newAgent.setLastName("Smith");
        updatedCase.setAgents(List.of(newAgent));

        Users user = new Users();

        when(caseFileRepository.findById(caseId)).thenReturn(Optional.of(oldCase));

        caseFileService.updateCaseFile(updatedCase, user);

        assertEquals("Updated Case", oldCase.getCaseName());
        assertEquals("Fraud", oldCase.getCaseCategory());
        assertEquals("Updated description", oldCase.getCaseDescription());
        assertEquals(updatedStation, oldCase.getStation());
        assertEquals(List.of(newAgent), oldCase.getAgents());
        assertEquals(user, oldCase.getLastUserAccess());
        assertNotNull(oldCase.getUpdatedAt());

        verify(caseVersionRepository).save(any(CaseVersion.class));
        verify(caseFileRepository).save(oldCase);
    }

}
