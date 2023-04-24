package sg.edu.nus.iss.server.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import sg.edu.nus.iss.server.models.Booking;
import sg.edu.nus.iss.server.models.Dreamer;
import sg.edu.nus.iss.server.models.PaymentResponse;
import sg.edu.nus.iss.server.repositories.DreamerRepository;

@Service
public class PaymentService {

    @Autowired
    private DreamerRepository dreamerRepo;

    @Autowired
    private APIContext apiContext;

    public static final String SERVER_HOST = "https://interstellarion-production.up.railway.app";
    public static final String CLIENT_HOST = "https://interstellarion.vercel.app/";

    // Creating payment based on booking details
    public PaymentResponse createPayment(Booking booking) {
        PaymentResponse response = new PaymentResponse();

        List<Transaction> transactions = getTransactions(booking);
       
        Payer payer = new Payer();
        payer = getPayerInformation(booking);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls = getRedirectUrls();

        Payment payment = new Payment();
        payment.setTransactions(transactions);
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setIntent("authorize");

        Payment createdPayment;
        try {
            String approvalLink = "";
            createdPayment = payment.create(apiContext);
            if (createdPayment != null) {
                approvalLink = getApprovalLink(createdPayment);
                response.setRedirectUrl(approvalLink);
                response.setStatus("success");
            }
        } catch (PayPalRESTException ex) {
            System.out.println("Error during payment creation.");
        }
        return response;
    }

    // Completing payment 
    public PaymentResponse completePayment(String paymentId, String payerId) {
        PaymentResponse response = new PaymentResponse();
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        try {
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            System.out.println(createdPayment);
            if (createdPayment != null) {
                response.setPayment(createdPayment);
                response.setStatus("success");
            }
        } catch (PayPalRESTException ex) {
            System.out.println(ex.getDetails());
        }
        return response;
    }

    // Get transactions
    private List<Transaction> getTransactions(Booking booking) {
        // Set Amount
        Amount amount = new Amount();
        amount.setCurrency("SGD");
        amount.setTotal(String.format("%.2f", booking.getTotalCost()));

        // Create Transaction (Pass amount and description)
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        String description = "Trip to %s".formatted(booking.getPlanet());
        transaction.setDescription(description);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        return transactions;
    }

    // Set Payer info 
    private Payer getPayerInformation(Booking booking) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        
        Dreamer dreamer = dreamerRepo.findDreamerById(booking.getDreamerId());
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(dreamer.getFirstName());
        payerInfo.setLastName(dreamer.getLastName());
        payerInfo.setEmail(dreamer.getEmail());
        
        payer.setPayerInfo(payerInfo);
        return payer;
    }

    // Set Redirect URLs (For cancelled or return payment)
    private RedirectUrls getRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        // If cancel payment:
        redirectUrls.setCancelUrl("https://interstellarion.vercel.app/cart");
        // If continue payment:
        redirectUrls.setReturnUrl("http://localhost:8080/checkout/complete");
        return redirectUrls;
    }

    // Get Approval Link
    private String getApprovalLink(Payment approvedPayment) {
        // Get list of links from approved payment
        List<Links> linksList = approvedPayment.getLinks();
        String approvalLink = null;
        for (Links links:linksList) {
            // If links = approval_url
            if (links.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = links.getHref();
            }
        }
        System.out.println(approvalLink);
        return approvalLink;
    }
}
