package com.example.soap.ws.Service;

public final class LoanCalculator {
    private LoanCalculator() {}

    public static double monthlyPayment(double principal, double annualRatePct, int months) {
        double r = annualRatePct / 100.0 / 12.0;
        if (r == 0) return principal / months;
        return principal * r / (1 - Math.pow(1 + r, -months));
    }

    public static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
