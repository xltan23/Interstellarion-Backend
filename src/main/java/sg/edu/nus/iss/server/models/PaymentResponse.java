package sg.edu.nus.iss.server.models;

import com.paypal.api.payments.Payment;

public class PaymentResponse {
    
    private String redirectUrl;
    private String status;
    private Payment payment;

    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Payment getPayment() {
        return payment;
    }
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
