package com.example.soap.ws.Web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.soap.ws.Service.InvalidLoanRequestException;
import com.example.soap.ws.Service.LoanEligibilityService;
import com.example.soap.ws.Service.LoanHistoryService;
import com.example.soap.ws.Service.LoanSimulationService;
import com.example.soap.ws.loaneligibility.Acknowledgement;
import com.example.soap.ws.loaneligibility.CustomerRequest;
import com.example.soap.ws.loaneligibility.LoanSimulationRequest;

@Controller
public class LoanWebController {

    private final LoanEligibilityService service;
    private final LoanSimulationService simulationService;
    private final LoanHistoryService history;

    public LoanWebController(LoanEligibilityService service, LoanSimulationService simulationService,
            LoanHistoryService history) {
        this.service = service;
        this.simulationService = simulationService;
        this.history = history;
    }

    @GetMapping("/")
    public String home(Model m) {
        fill(m);
        return "index";
    }

    @PostMapping("/check")
    public String check(@RequestParam String customerName, @RequestParam int age,
            @RequestParam long yearlyIncome, @RequestParam int cibilScore,
            @RequestParam String employmentMode, Model m) {
        CustomerRequest r = new CustomerRequest();
        r.setCustomerName(customerName);
        r.setAge(age);
        r.setYearlyIncome(yearlyIncome);
        r.setCibilScore(cibilScore);
        r.setEmploymentMode(employmentMode);
        try {
            Acknowledgement a = service.checkLoanEligibility(r);
            m.addAttribute("ack", a);
            m.addAttribute("eligible", a.isIsEligible());
        } catch (InvalidLoanRequestException e) {
            m.addAttribute("error", e.getMessage());
        }
        fill(m);
        return "index";
    }

    @PostMapping("/simulate")
    public String simulate(@RequestParam double amount, @RequestParam double rate,
            @RequestParam int months, Model m) {
        LoanSimulationRequest q = new LoanSimulationRequest();
        q.setAmount(amount);
        q.setAnnualRatePercent(rate);
        q.setDurationMonths(months);
        try {
            m.addAttribute("sim", simulationService.simulate(q));
        } catch (InvalidLoanRequestException e) {
            m.addAttribute("error", e.getMessage());
        }
        fill(m);
        return "index";
    }

    private void fill(Model m) {
        m.addAttribute("history", history.find(null));
        m.addAttribute("stats", history.stats());
    }
}
