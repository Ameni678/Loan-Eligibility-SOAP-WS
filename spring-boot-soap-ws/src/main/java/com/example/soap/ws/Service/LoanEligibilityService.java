package com.example.soap.ws.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.soap.ws.loaneligibility.Acknowledgement;
import com.example.soap.ws.loaneligibility.CustomerRequest;

@Service
public class LoanEligibilityService {

    private static final int DEFAULT_DURATION = 60;
    private static final long MAX_AMOUNT = 5_000_000;
    private static final List<String> ACCEPTED_MODES = List.of("GOV", "SALARIED", "SELF_EMPLOYED");

    private final LoanHistoryService history;

    public LoanEligibilityService(LoanHistoryService history) {
        this.history = history;
    }

    public Acknowledgement checkLoanEligibility(CustomerRequest r) {
        validate(r);
        Acknowledgement ack = new Acknowledgement();
        List<String> mismatch = ack.getCriteriaMismatch();
        String mode = r.getEmploymentMode().trim().toUpperCase();

        if (r.getAge() < 21 || r.getAge() > 60)
            mismatch.add("L'âge doit être compris entre 21 et 60 ans");
        if (r.getYearlyIncome() < 200_000)
            mismatch.add("Le revenu annuel minimum est de 200 000");
        if (r.getCibilScore() < 500)
            mismatch.add("Score CIBIL trop faible (minimum 500), réessayez dans 6 mois");
        if (!ACCEPTED_MODES.contains(mode))
            mismatch.add("Mode d'emploi non accepté : " + mode + " (GOV, SALARIED, SELF_EMPLOYED)");

        String risk = r.getCibilScore() >= 750 ? "LOW" : r.getCibilScore() >= 650 ? "MEDIUM" : "HIGH";
        ack.setRiskLevel(risk);

        if (!mismatch.isEmpty()) {
            ack.setIsEligible(false);
            ack.setApprovedAmount(0);
        } else {
            double incomeFactor = switch (mode) {
                case "GOV" -> 4.0;
                case "SALARIED" -> 3.0;
                default -> 2.0;
            };
            double riskFactor = switch (risk) {
                case "LOW" -> 1.0;
                case "MEDIUM" -> 0.8;
                default -> 0.5;
            };
            double rate = switch (risk) {
                case "LOW" -> 8.5;
                case "MEDIUM" -> 10.5;
                default -> 13.0;
            };

            long amount = Math.round(r.getYearlyIncome() * incomeFactor * riskFactor / 1000.0) * 1000;
            amount = Math.min(amount, MAX_AMOUNT);

            ack.setIsEligible(true);
            ack.setApprovedAmount(amount);
            ack.setInterestRate(rate);
            ack.setMonthlyPayment(LoanCalculator.round2(
                    LoanCalculator.monthlyPayment(amount, rate, DEFAULT_DURATION)));
        }
        history.save(r, ack);
        return ack;
    }

    private void validate(CustomerRequest r) {
        if (r == null) throw new InvalidLoanRequestException("Requête vide");
        if (r.getCustomerName() == null || r.getCustomerName().isBlank())
            throw new InvalidLoanRequestException("Le nom du client est obligatoire");
        if (r.getAge() <= 0 || r.getAge() > 120)
            throw new InvalidLoanRequestException("Âge invalide : " + r.getAge());
        if (r.getYearlyIncome() < 0)
            throw new InvalidLoanRequestException("Le revenu ne peut pas être négatif");
        if (r.getCibilScore() < 300 || r.getCibilScore() > 900)
            throw new InvalidLoanRequestException("Le score CIBIL doit être entre 300 et 900");
        if (r.getEmploymentMode() == null || r.getEmploymentMode().isBlank())
            throw new InvalidLoanRequestException("Le mode d'emploi est obligatoire");
    }
}
