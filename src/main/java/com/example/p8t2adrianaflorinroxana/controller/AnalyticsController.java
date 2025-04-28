package com.example.p8t2adrianaflorinroxana.controller;

import com.example.p8t2adrianaflorinroxana.service.AnalyticsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsServiceImpl analyticsService;

    public AnalyticsController(AnalyticsServiceImpl analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public String getDashboardData(@RequestParam(required = false) Integer month,
                                   @RequestParam(required = false) Integer year,
                                   Model model) {

        Map<String, Object> dashboardData = analyticsService.getCrimeStatistics(month, year);

        model.addAttribute("years", IntStream.range(Year.now().getValue() - 10, Year.now().getValue() + 1).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList()));
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        model.addAttribute("months", Month.values());
        model.addAttribute("dashboardData", dashboardData);

        return "Analytics/Dashboard";
    }

    @GetMapping("/report")
    public String showTrendReport(@RequestParam(required = false) Integer year,
                                  Model model) {

        Map<String, Object> trendsData = analyticsService.getCrimeTrends(year);

        model.addAttribute("years", IntStream.range(Year.now().getValue() - 10, Year.now().getValue() + 1).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList()));
        model.addAttribute("selectedYear", year);
        model.addAttribute("trendsData", trendsData);

        return "Analytics/Report";
    }


}