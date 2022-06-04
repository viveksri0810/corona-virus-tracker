package com.vivek.cvt.controller;

import com.vivek.cvt.service.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    @Autowired
    CoronaVirusDataService cvds;

    @GetMapping("/home")
    public String getDashboardData(Model model){
        model.addAttribute("summaryStats",cvds.getSummaryStats());
        model.addAttribute("modelStats",cvds.getModelStatsList());
        return "home";
    }

}
