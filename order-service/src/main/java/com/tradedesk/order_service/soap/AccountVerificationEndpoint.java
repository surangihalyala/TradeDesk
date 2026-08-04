package com.tradedesk.order_service.soap;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AccountVerificationEndpoint {

    private static final String NAMESPACE_URI = "http://tradedesk.com/accountverification";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "accountVerificationRequest")
    @ResponsePayload
    public AccountVerificationResponse verifyAccount(@RequestPayload AccountVerificationRequest request) {
        AccountVerificationResponse response = new AccountVerificationResponse();
        response.setAccountId(request.getAccountId());

        boolean verified = request.getAccountId() != null && request.getAccountId().startsWith("ACC");
        response.setVerified(verified);
        response.setMessage(verified ? "Account verified" : "Account not found");

        return response;
    }
}
