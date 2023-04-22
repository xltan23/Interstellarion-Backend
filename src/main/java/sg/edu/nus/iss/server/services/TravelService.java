package sg.edu.nus.iss.server.services;

import java.io.StringReader;
import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.server.models.Booking;
import sg.edu.nus.iss.server.repositories.TravelRepository;

@Service
public class TravelService {
    
    @Autowired
    private TravelRepository travelRepo;

    public void saveTemporaryBooking(Booking booking) {
        String payload = booking.toJSON().toString();
        travelRepo.save(booking.getDreamerId(), payload);
    }

    public Booking getTemporaryBooking(String dreamerId) throws ParseException {
        Optional<String> optBook = travelRepo.get(dreamerId);
        Booking booking = new Booking();
        if (optBook.isEmpty()) {
            return booking;
        }
        String jsonString = optBook.get();
        StringReader sr = new StringReader(jsonString);
        JsonReader jr = Json.createReader(sr);
        JsonObject jo = jr.readObject();
        return Booking.create(jo);
    }
}
