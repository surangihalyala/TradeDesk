package com.tradedesk.order_service.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "accountVerificationRequest", namespace = "http://tradedesk.com/accountverification")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountVerificationRequest {

    @XmlElement(namespace = "http://tradedesk.com/accountverification")
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
