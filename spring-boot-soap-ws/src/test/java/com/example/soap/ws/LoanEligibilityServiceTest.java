package com.example.soap.ws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import com.example.soap.ws.Service.InvalidLoanRequestException;
import com.example.soap.ws.Service.LoanEligibilityService;
import com.example.soap.ws.Service.LoanHistoryService;
import com.example.soap.ws.loaneligibility.Acknowledgement;
import com.example.soap.ws.loaneligibility.CustomerRequest;

class LoanEligibilityServiceTest {

    private final LoanEligibilityService service =
            new LoanEligibilityService(new LoanHistoryService());

    private CustomerRequest req(int age, long income, int score, String mode) {
        CustomerRequest r = new CustomerRequest();
        r.setCustomerName("Test");
        r.setAge(age);
        r.setYearlyIncome(income);
        r.setCibilScore(score);
        r.setEmploymentMode(mode);
        return r;
    }

    @Test
    void clientEligible() {
        Acknowledgement a = service.checkLoanEligibility(req(40, 300_000, 780, "GOV"));
        assertTrue(a.isIsEligible());
        assertEquals("LOW", a.getRiskLevel());
        assertTrue(a.getApprovedAmount() > 0);
    }

    @Test
    void clientRefuseAvecPlusieursRaisons() {
        Acknowledgement a = service.checkLoanEligibility(req(70, 100_000, 400, "UNEMPLOYED"));
        assertFalse(a.isIsEligible());
        assertEquals(4, a.getCriteriaMismatch().size());
    }

    @Test
    void donneesInvalides() {
        assertThrows(InvalidLoanRequestException.class,
                () -> service.checkLoanEligibility(req(-5, 300_000, 700, "GOV")));
    }
}
