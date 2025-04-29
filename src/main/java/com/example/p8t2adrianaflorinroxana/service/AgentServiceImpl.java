package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.repository.AgentRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AgentServiceImpl {
    private final AgentRepository agentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleServiceImpl roleService;

    public AgentServiceImpl(AgentRepository agentRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleServiceImpl roleService) {
        this.agentRepository = agentRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }

    public void addAgent(Agents agent, Stations station, String roleName) {

        agent.setStation(station);
        agent.getUser().setPassword(bCryptPasswordEncoder.encode(agent.getUser().getPassword()));
        agent.getUser().setRoles(Collections.singleton(roleService.findByName(roleName)));
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

    public Agents getAgentByEmail(String email) {

        return agentRepository.findByEmail(email);
    }

    public void changePassword(String email, String password) {

        Agents agent = agentRepository.findByEmail(email);
        if(agent != null){
            agent.getUser().setPassword(bCryptPasswordEncoder.encode(password));
            agentRepository.save(agent);
        }
    }
}
