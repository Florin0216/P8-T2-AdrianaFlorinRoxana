package com.example.p8t2adrianaflorinroxana.utils;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HierarchyData {
    private static final List<String> OFFICER_RANKS = List.of(
            "General-Quaestor", "Chief-Quaestor", "Principal Quaestor",
            "Quaestor", "Chief-Commissioner", "Commissioner",
            "Sub-Commissioner", "Principal Inspector", "Inspector", "Sub-Inspector"
    );

    private static final List<String> AGENT_RANKS = List.of(
            "Principal Chief Agent", "Chief Agent", "Deputy Chief Agent",
            "Principal Agent", "Agent"
    );

    private final List<String> displayedOfficerRanks;
    private final List<String> displayedAgentRanks;
    private final Map<String, Map<String, List<Agents>>> groupedAgents;

    public HierarchyData(List<Agents> agents) {
        this.groupedAgents = groupAgentsByCorpsAndRank(agents);
        this.displayedOfficerRanks = filterExistingRanks("Officer", OFFICER_RANKS);
        this.displayedAgentRanks = filterExistingRanks("Agent", AGENT_RANKS);
    }

    private Map<String, Map<String, List<Agents>>> groupAgentsByCorpsAndRank(List<Agents> agents) {
        return agents.stream()
                .collect(Collectors.groupingBy(
                        Agents::getCorps,
                        Collectors.groupingBy(Agents::getRank)
                ));
    }

    private List<String> filterExistingRanks(String corps, List<String> rankOrder) {
        if (!groupedAgents.containsKey(corps)) {
            return Collections.emptyList();
        }

        return rankOrder.stream()
                .filter(rank -> {
                    Map<String, List<Agents>> corpsRanks = groupedAgents.get(corps);
                    return corpsRanks.containsKey(rank) &&
                            corpsRanks.get(rank) != null &&
                            !corpsRanks.get(rank).isEmpty();
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> toModelAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("displayedOfficerRanks", displayedOfficerRanks);
        attributes.put("displayedAgentRanks", displayedAgentRanks);
        attributes.put("groupedAgents", groupedAgents);
        attributes.put("officerRankOrder", OFFICER_RANKS);
        attributes.put("agentRankOrder", AGENT_RANKS);
        return attributes;
    }

    public static HierarchyData organizeHierarchyData(List<Agents> agents) {
        return new HierarchyData(agents);
    }
}
