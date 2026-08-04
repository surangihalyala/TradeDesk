package com.tradedesk.order_service.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "accountVerificationResponse", namespace = "http://tradedesk.com/accountverification")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountVerificationResponse {

    @XmlElement(namespace = "http://tradedesk.com/accountverification")
    private String accountId;

    @XmlElement(namespace = "http://tradedesk.com/accountverification")
    private boolean verified;

    @XmlElement(namespace = "http://tradedesk.com/accountverification")
    private String message;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
