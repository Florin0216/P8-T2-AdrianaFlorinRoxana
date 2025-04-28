package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.AgentRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Agents> getAllAgentsForStation(long id){
        return agentRepository.findByStation_Id(id);
    }

    @Transactional
    public void deleteAgent(long id){
        agentRepository.deleteById(id);
    }

    public List<Agents> getAllAgents() {
        return agentRepository.findAll();
    }

    public List<Agents> getAgentByName(String firstName,String lastName) {

        return agentRepository.findAllByFirstNameOrLastName(firstName,lastName);
    }

}
