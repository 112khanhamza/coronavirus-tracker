package com.hamza.controller;

import com.hamza.model.LatestStat;
import com.hamza.service.CoronavirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private CoronavirusDataService coronavirusDataService;

    @GetMapping("/")
    public String home(Model model) {

        List<LatestStat> allStats = coronavirusDataService.getAllStats().stream().
                sorted(Comparator.comparingInt(LatestStat::getLatestCount).reversed())
                .collect(Collectors.toList());

        allStats.stream()
                .forEach(x -> {
            if (x.getState().equals("")) {
                x.setState("NONE");
            }
        });

        int totalRecord = allStats.stream().mapToInt(x -> x.getLatestCount()).sum();

        model.addAttribute("latestRecords", allStats);
        model.addAttribute("totalRecord", totalRecord);

        return "home";
    }
}
