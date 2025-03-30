package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.repository.AgentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AgentServiceImpl {
    private final AgentRepository agentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AgentServiceImpl(AgentRepository agentRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.agentRepository = agentRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void addAgent(Agents agent){
        String hashedPassword = bCryptPasswordEncoder.encode(agent.getUser().getPassword());
        agent.getUser().setPassword(hashedPassword);
        agentRepository.save(agent);
    }

}
