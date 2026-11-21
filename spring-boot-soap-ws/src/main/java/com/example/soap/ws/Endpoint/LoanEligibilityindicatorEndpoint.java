package com.example.soap.ws.Endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import com.example.soap.ws.Service.LoanEligibilityService;
import com.example.soap.ws.Service.LoanHistoryService;
import com.example.soap.ws.Service.LoanSimulationService;
import com.example.soap.ws.loaneligibility.Acknowledgement;
import com.example.soap.ws.loaneligibility.CustomerRequest;
import com.example.soap.ws.loaneligibility.LoanHistoryRequest;
import com.example.soap.ws.loaneligibility.LoanHistoryResponse;
import com.example.soap.ws.loaneligibility.LoanSimulationRequest;
import com.example.soap.ws.loaneligibility.LoanSimulationResponse;
import com.example.soap.ws.loaneligibility.LoanStatsRequest;
import com.example.soap.ws.loaneligibility.LoanStatsResponse;

@Endpoint
public class LoanEligibilityindicatorEndpoint {

    private static final String NAMESPACE = "http://www.example.com/soap/ws/loanEligibility";

    private final LoanEligibilityService service;
    private final LoanSimulationService simulationService;
    private final LoanHistoryService historyService;

    public LoanEligibilityindicatorEndpoint(LoanEligibilityService service,
            LoanSimulationService simulationService, LoanHistoryService historyService) {
        this.service = service;
        this.simulationService = simulationService;
        this.historyService = historyService;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "CustomerRequest")
    @ResponsePayload
    public Acknowledgement getLoanStatus(@RequestPayload CustomerRequest request) {
        return service.checkLoanEligibility(request);
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "LoanSimulationRequest")
    @ResponsePayload
    public LoanSimulationResponse simulate(@RequestPayload LoanSimulationRequest request) {
        return simulationService.simulate(request);
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "LoanHistoryRequest")
    @ResponsePayload
    public LoanHistoryResponse history(@RequestPayload LoanHistoryRequest request) {
        LoanHistoryResponse res = new LoanHistoryResponse();
        res.getRecord().addAll(historyService.find(request.getCustomerName()));
        return res;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "LoanStatsRequest")
    @ResponsePayload
    public LoanStatsResponse stats(@RequestPayload LoanStatsRequest request) {
        return historyService.stats();
    }
}
