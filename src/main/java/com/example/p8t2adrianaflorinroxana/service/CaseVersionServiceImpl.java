package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.CaseVersion;
import com.example.p8t2adrianaflorinroxana.repository.CaseVersionRepository;
import org.springframework.stereotype.Service;

@Service
public class CaseVersionServiceImpl {

    private final CaseVersionRepository caseVersionRepository;

    public CaseVersionServiceImpl(CaseVersionRepository caseVersionRepository) {
        this.caseVersionRepository = caseVersionRepository;
    }

    public CaseVersion findVersionById(long id){
        return caseVersionRepository.findById(id).get();
    }
}
