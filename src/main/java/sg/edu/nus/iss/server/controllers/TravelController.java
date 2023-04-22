package sg.edu.nus.iss.server.controllers;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.server.models.Booking;
import sg.edu.nus.iss.server.security.HttpResponse;
import sg.edu.nus.iss.server.services.TravelService;

@RestController
@RequestMapping(path = "/travel")
@CrossOrigin(origins = "*")
public class TravelController {
    
    @Autowired
    private TravelService travelSvc;

    @PostMapping("/save")
    public ResponseEntity<HttpResponse> saveTemporaryBooking(@RequestBody Booking booking) {
        travelSvc.saveTemporaryBooking(booking);
        String message = "Booking saved.";
        return response(HttpStatus.OK, message);
    }

    @GetMapping("{dreamerId}")
    public ResponseEntity<Booking> getTemporaryBooking(@PathVariable String dreamerId) throws ParseException {
        Booking booking = travelSvc.getTemporaryBooking(dreamerId);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(), message), httpStatus);
    }
}
