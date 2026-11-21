package com.example.soap.ws.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;
import com.example.soap.ws.loaneligibility.Acknowledgement;
import com.example.soap.ws.loaneligibility.CustomerRequest;
import com.example.soap.ws.loaneligibility.HistoryRecord;
import com.example.soap.ws.loaneligibility.LoanStatsResponse;

@Service
public class LoanHistoryService {

    private final List<HistoryRecord> records = new CopyOnWriteArrayList<>();

    public void save(CustomerRequest r, Acknowledgement a) {
        HistoryRecord h = new HistoryRecord();
        h.setCustomerName(r.getCustomerName());
        h.setEligible(a.isIsEligible());
        h.setApprovedAmount(a.getApprovedAmount());
        h.setRiskLevel(a.getRiskLevel());
        h.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        records.add(h);
    }

    public List<HistoryRecord> find(String name) {
        return records.stream()
                .filter(h -> name == null || name.isBlank() || h.getCustomerName().equalsIgnoreCase(name))
                .toList();
    }

    public LoanStatsResponse stats() {
        int total = records.size();
        int ok = (int) records.stream().filter(HistoryRecord::isEligible).count();
        LoanStatsResponse s = new LoanStatsResponse();
        s.setTotalRequests(total);
        s.setApproved(ok);
        s.setRejected(total - ok);
        s.setApprovalRate(total == 0 ? 0 : LoanCalculator.round2(100.0 * ok / total));
        s.setTotalApprovedAmount(records.stream().mapToLong(HistoryRecord::getApprovedAmount).sum());
        return s;
    }
}
