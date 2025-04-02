package com.example.p8t2adrianaflorinroxana.model;

public enum AgentRank {
    General_Quaestor(AgentCorps.Officer),
    Chief_Quaestor(AgentCorps.Officer),
    Principal_Quaestor(AgentCorps.Officer),
    Quaestor(AgentCorps.Officer),
    Chief_Commissioner(AgentCorps.Officer),
    Commissioner(AgentCorps.Officer),
    Sub_Commissioner(AgentCorps.Officer),
    Principal_Inspector(AgentCorps.Officer),
    Inspector(AgentCorps.Officer),
    Sub_Inspector(AgentCorps.Officer),

    Principal_Chief_Agent(AgentCorps.Agent),
    Chief_Agent(AgentCorps.Agent),
    Deputy_Chief_Agent(AgentCorps.Agent),
    Principal_Agent(AgentCorps.Agent),
    Agent(AgentCorps.Agent);

    private final AgentCorps agentCorps;

    AgentRank(AgentCorps agentCorps) {
        this.agentCorps = agentCorps;
    }

    public AgentCorps getCorps() {
        return agentCorps;
    }

}
