package sg.edu.nus.iss.server.repositories;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.server.models.Booking;

import static sg.edu.nus.iss.server.repositories.Queries.*;

@Repository
public class TravelRepository {
    
    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Save unpaid booking temporarily for 15 minutes
    public void save(String key, String payload) {
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, payload, Duration.ofMinutes(15));
    }

    public Optional<String> get(String key) {
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        String payload = valueOps.get(key);
        if (null == payload) {
            return Optional.empty();
        }
        return Optional.of(payload);
    }

    public void delete(String key) {
        ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
        valueOps.getAndDelete(key);
    }

    public List<Booking> getBookings(String dreamerId) {
        SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_SELECT_BOOKINGS, dreamerId);
        List<Booking> bookingList = new LinkedList<>();
        while (srs.next()) {
            bookingList.add(Booking.create(srs));
        }
        return bookingList;
    } 

    public boolean insertBooking(Booking booking) {
        int bookingInserted = jdbcTemplate.update(SQL_INSERT_NEW_BOOKING, booking.getDreamerId(), booking.getPlanet(), 
            booking.getNumberOfPax(), booking.getStringDate(), booking.getTotalCost());
        return bookingInserted > 0;
    }
}
