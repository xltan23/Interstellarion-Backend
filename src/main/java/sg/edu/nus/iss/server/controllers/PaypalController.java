package sg.edu.nus.iss.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paypal.base.rest.PayPalRESTException;

import sg.edu.nus.iss.server.models.Booking;
import sg.edu.nus.iss.server.models.PaymentResponse;
import sg.edu.nus.iss.server.services.PaymentService;

@RestController
@RequestMapping(value = "/checkout")
@CrossOrigin(origins = "*")
public class PaypalController {
    
    @Autowired
    private PaymentService paymentSvc;

    @PostMapping("/pay")
    public PaymentResponse checkoutBooking(@RequestBody Booking booking) throws PayPalRESTException {
        return paymentSvc.createPayment(booking);
    }

    @GetMapping("/complete")
    public ModelAndView completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        PaymentResponse paymentResponse = paymentSvc.completePayment(paymentId, payerId);
        String url = "http://localhost:4200/dreamer";
        return new ModelAndView("redirect:" + url);
    }
}
