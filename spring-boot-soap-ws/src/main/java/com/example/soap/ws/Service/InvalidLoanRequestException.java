package com.example.soap.ws.Service;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/** Transformee automatiquement en SOAP Fault (faultcode Client). */
@SoapFault(faultCode = FaultCode.CLIENT)
public class InvalidLoanRequestException extends RuntimeException {
    public InvalidLoanRequestException(String message) {
        super(message);
    }
}
