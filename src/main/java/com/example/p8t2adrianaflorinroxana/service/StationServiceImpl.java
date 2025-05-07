package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.Agents;
import com.example.p8t2adrianaflorinroxana.model.Stations;
import com.example.p8t2adrianaflorinroxana.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Service
public class StationServiceImpl {

    private final StationRepository stationRepository;

    public StationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void saveStation(Stations station){
        stationRepository.save(station);
    }

    public List<Stations> getAllStations() {
        return stationRepository.findAll();
    }

    public Stations getStationById(long id) {
        return stationRepository.findStationsById(id);
    }

    public void editStation(Stations station) {
        Stations existingStation = stationRepository.findStationsById(station.getId());
        existingStation.setStationName(station.getStationName());
        existingStation.setLatitude(station.getLatitude());
        existingStation.setLongitude(station.getLongitude());
        existingStation.setStationAddress(station.getStationAddress());
        existingStation.setStationPhone(station.getStationPhone());
        stationRepository.save(existingStation);
    }

    public void deleteStation(long id) {
        stationRepository.deleteById(id);
    }


    public void exportStationsWithAgentsToCSV(PrintWriter writer, List<Stations> stations, Map<Long, List<Agents>> agentsByStation) {
        for (Stations station : stations) {
            writer.printf("%s,%f,%f%n",
                    station.getStationName(),
                    station.getLatitude(),
                    station.getLongitude());

            List<Agents> agents = agentsByStation.get(station.getId());
            if (agents != null) {
                for (Agents agent : agents) {
                    writer.printf(",%s,%s,%s,%s%n",
                            agent.getFirstName(),
                            agent.getLastName(),
                            agent.getRank().name(),
                            agent.getRank().getCorps().name());
                }
            }
        }
    }
}
