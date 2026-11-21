package com.example.soap.ws.Service;

import static com.example.soap.ws.Service.LoanCalculator.monthlyPayment;
import static com.example.soap.ws.Service.LoanCalculator.round2;

import org.springframework.stereotype.Service;
import com.example.soap.ws.loaneligibility.Installment;
import com.example.soap.ws.loaneligibility.LoanSimulationRequest;
import com.example.soap.ws.loaneligibility.LoanSimulationResponse;

@Service
public class LoanSimulationService {

    public LoanSimulationResponse simulate(LoanSimulationRequest q) {
        if (q.getAmount() <= 0) throw new InvalidLoanRequestException("Le montant doit être > 0");
        if (q.getAnnualRatePercent() < 0) throw new InvalidLoanRequestException("Taux invalide");
        if (q.getDurationMonths() < 1 || q.getDurationMonths() > 360)
            throw new InvalidLoanRequestException("Durée entre 1 et 360 mois");

        int n = q.getDurationMonths();
        double pay = monthlyPayment(q.getAmount(), q.getAnnualRatePercent(), n);
        double r = q.getAnnualRatePercent() / 100.0 / 12.0;
        double balance = q.getAmount();

        LoanSimulationResponse res = new LoanSimulationResponse();
        for (int i = 1; i <= n; i++) {
            double interest = balance * r;
            double principal = pay - interest;
            balance -= principal;
            Installment in = new Installment();
            in.setNumber(i);
            in.setPayment(round2(pay));
            in.setPrincipal(round2(principal));
            in.setInterest(round2(interest));
            in.setRemaining(round2(Math.max(balance, 0)));
            res.getInstallment().add(in);
        }
        res.setMonthlyPayment(round2(pay));
        res.setTotalCost(round2(pay * n));
        res.setTotalInterest(round2(pay * n - q.getAmount()));
        return res;
    }
}
