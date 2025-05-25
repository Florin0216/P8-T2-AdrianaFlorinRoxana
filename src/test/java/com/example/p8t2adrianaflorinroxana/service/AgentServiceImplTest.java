package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Roles;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.model.Users;
import com.example.p8t2adrianaflorinroxana.repository.AgentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentServiceImplTest {

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleServiceImpl roleService;

    @InjectMocks
    private AgentServiceImpl agentService;

    @Test
    void addAgent() {
        Agents agent = new Agents();
        Users user = new Users();
        user.setPassword("plain123");
        agent.setUser(user);

        Stations station = new Stations();
        String roleName = "AGENT";

        Roles mockRole = new Roles();
        when(passwordEncoder.encode("plain123")).thenReturn("encoded123");
        when(roleService.findByName(roleName)).thenReturn(mockRole);

        agentService.addAgent(agent, station, roleName);

        assertEquals("encoded123", agent.getUser().getPassword());
        assertEquals(station, agent.getStation());
        assertEquals(Collections.singleton(mockRole), agent.getUser().getRoles());
        verify(agentRepository).save(agent);
    }

    @Test
    void changePassword() {
        String email = "agent@example.com";
        String newPassword = "newPassword123";
        String encodedPassword = "encodedNewPassword";

        Users user = new Users();
        user.setPassword("oldPassword");

        Agents agent = new Agents();
        agent.setUser(user);

        when(agentRepository.findByEmail(email)).thenReturn(agent);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        agentService.changePassword(email, newPassword);

        assertEquals(encodedPassword, agent.getUser().getPassword());
        verify(agentRepository).save(agent);
    }

    @Test
    void updateAgent() {
        long agentId = 1L;

        Agents existing = new Agents();
        existing.setId(agentId);

        Agents updated = new Agents();
        updated.setFirstName("John");
        updated.setLastName("Doe");
        updated.setGender("Male");
        updated.setStatus("Active");
        updated.setEmail("john.doe@example.com");
        updated.setPhoneNumber(1234567890);
        updated.setAddress("123 Street");

        when(agentRepository.findById(agentId)).thenReturn(java.util.Optional.of(existing));

        agentService.updateAgent(agentId, updated);

        assertEquals("John", existing.getFirstName());
        assertEquals("Doe", existing.getLastName());
        assertEquals("Male", existing.getGender());
        assertEquals("Active", existing.getStatus());
        assertEquals("john.doe@example.com", existing.getEmail());
        assertEquals(1234567890, existing.getPhoneNumber());
        assertEquals("123 Street", existing.getAddress());

        verify(agentRepository).save(existing);
    }
}