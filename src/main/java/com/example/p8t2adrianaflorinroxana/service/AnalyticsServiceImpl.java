package com.example.p8t2adrianaflorinroxana.service;

import com.example.p8t2adrianaflorinroxana.model.CaseFiles;
import com.example.p8t2adrianaflorinroxana.repository.CaseFileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl {

    private final CaseFileRepository caseFilesRepository;

    public AnalyticsServiceImpl(CaseFileRepository caseFilesRepository) {
        this.caseFilesRepository = caseFilesRepository;
    }

    public Map<String, Object> getCrimeStatistics(Integer month, Integer year) {
        List<CaseFiles> cases;
        if (month != null && year != null) {
            LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);
            cases = caseFilesRepository.findByCreatedAtBetween(startDate, endDate);
        } else if (year != null) {
            LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
            LocalDateTime endDate = startDate.plusYears(1).minusSeconds(1);
            cases = caseFilesRepository.findByCreatedAtBetween(startDate, endDate);
        } else {
            cases = caseFilesRepository.findAll();
        }

        Map<String, Object> statistics = new HashMap<>();

        statistics.put("crimeRate", null);
        statistics.put("crimeRateDescription", null);
        statistics.put("totalCases", cases.size());

        long solvedCases = cases.stream().filter(c -> "SOLVED".equalsIgnoreCase(c.getStatus())).count();
        statistics.put("solvedCases", solvedCases);
        statistics.put("unsolvedCases", cases.size() - solvedCases);

        Map<String, Long> casesByCategory = cases.stream()
                .collect(Collectors.groupingBy(CaseFiles::getCaseCategory, Collectors.counting()));

        Map<String, Long> sortedCasesByCategory = casesByCategory.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        statistics.put("casesByCategory", sortedCasesByCategory);

        Map<String, Long> casesByStation = cases.stream()
                .filter(c -> c.getStation() != null)
                .collect(Collectors.groupingBy(c -> c.getStation().getStationName(), Collectors.counting()));

        Map<String, Long> sortedCasesByStation = casesByStation.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        statistics.put("casesByStation", sortedCasesByStation);

        if (month != null && year != null) {
            int daysInMonth = LocalDateTime.of(year, month, 1, 0, 0).toLocalDate().lengthOfMonth();
            double crimeRate = (double) cases.size() / daysInMonth;
            statistics.put("crimeRate", String.format("%.2f", crimeRate));
            statistics.put("crimeRateDescription", "cases per day this month");
        } else if (year != null) {
            double crimeRate = (double) cases.size() / 365;
            statistics.put("crimeRate", String.format("%.2f", crimeRate));
            statistics.put("crimeRateDescription", "cases per day this year");
        }

        Map<String, Long> locationCounts = cases.stream()
                .filter(c -> c.getCaseLocation() != null && !c.getCaseLocation().isEmpty())
                .collect(Collectors.groupingBy(
                        c -> c.getCaseLocation().toUpperCase(),
                        Collectors.counting()
                ));

        Map<String, Long> crimeHotspots = locationCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        statistics.put("crimeHotspots", crimeHotspots);

        return statistics;
    }

    public Map<String, Object> getCrimeTrends(Integer year) {
        Map<String, Object> trendsData = new HashMap<>();

        List<CaseFiles> cases;
        if (year != null) {
            LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
            LocalDateTime endDate = startDate.plusYears(1).minusSeconds(1);
            cases = caseFilesRepository.findByCreatedAtBetween(startDate, endDate);
        } else {
            cases = caseFilesRepository.findAll();
        }

        long totalCases = cases.size();
        trendsData.put("totalCases", totalCases);

        Map<String, Long> monthlyCounts = cases.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getCreatedAt().getMonth().name(),
                        Collectors.counting()
                ));

        Map<String, Map<String, Object>> monthlyTrends = new LinkedHashMap<>();
        for (Month month : Month.values()) {
            long count = monthlyCounts.getOrDefault(month.name(), 0L);
            double percentage = totalCases > 0 ? (count * 100.0 / totalCases) : 0;

            Map<String, Object> monthData = new HashMap<>();
            monthData.put("count", count);
            monthData.put("percentage", percentage);
            monthData.put("percentageFormatted", String.format("%.1f%%", percentage));
            monthlyTrends.put(month.name(), monthData);
        }
        trendsData.put("monthlyTrends", monthlyTrends);

        if (year != null) {
            Map<String, Object> yearlyComparison = new LinkedHashMap<>();

            LocalDateTime prevYearStart = LocalDateTime.of(year-1, 1, 1, 0, 0);
            LocalDateTime prevYearEnd = prevYearStart.plusYears(1).minusSeconds(1);
            long prevYearCount = caseFilesRepository.countByCreatedAtBetween(prevYearStart, prevYearEnd);
            yearlyComparison.put("previousYear", prevYearCount);

            yearlyComparison.put("currentYear", totalCases);

            if (prevYearCount > 0) {
                double changePercent = ((totalCases - prevYearCount) * 100.0 / prevYearCount);
                yearlyComparison.put("changePercent", changePercent);
                yearlyComparison.put("changePercentFormatted",
                        String.format("%.1f%% %s", Math.abs(changePercent),
                                changePercent >= 0 ? "increase" : "decrease"));
            } else {
                yearlyComparison.put("changePercentFormatted", "N/A (no previous year data)");
            }

            trendsData.put("yearlyComparison", yearlyComparison);
        }

        Map<String, Long> crimeTypeCounts = cases.stream()
                .collect(Collectors.groupingBy(
                        CaseFiles::getCaseCategory,
                        Collectors.counting()
                ));

        Map<String, Map<String, Object>> crimeTypeTrends = new LinkedHashMap<>();
        crimeTypeCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    double percentage = totalCases > 0 ? (entry.getValue() * 100.0 / totalCases) : 0;
                    Map<String, Object> typeData = new HashMap<>();
                    typeData.put("count", entry.getValue());
                    typeData.put("percentage", percentage);
                    typeData.put("percentageFormatted", String.format("%.1f%%", percentage));
                    crimeTypeTrends.put(entry.getKey(), typeData);
                });

        trendsData.put("crimeTypeTrends", crimeTypeTrends);

        Optional<Map.Entry<String, Map<String, Object>>> peakMonth = monthlyTrends.entrySet().stream()
                .max(Comparator.comparingDouble(e -> (Double) e.getValue().get("percentage")));

        peakMonth.ifPresent(entry -> {
            trendsData.put("peakMonthName", entry.getKey());
            trendsData.put("peakMonthCount", entry.getValue().get("count"));
            trendsData.put("peakMonthPercentage", entry.getValue().get("percentageFormatted"));
        });

        Optional<Map.Entry<String, Map<String, Object>>> commonCrime = crimeTypeTrends.entrySet().stream()
                .max(Comparator.comparingDouble(e -> (Double) e.getValue().get("percentage")));

        commonCrime.ifPresent(entry -> {
            trendsData.put("commonCrimeName", entry.getKey());
            trendsData.put("commonCrimeCount", entry.getValue().get("count"));
            trendsData.put("commonCrimePercentage", entry.getValue().get("percentageFormatted"));
        });

        return trendsData;
    }

}