package sg.edu.nus.iss.server.models;

import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Booking {
    
    // Booking Attributes
    private String dreamerId;
    private String planet;
    private String planetThumbnail;
    private Integer numberOfPax;
    private Date travelDate;
    private String stringDate;
    private Double totalCost;
    private Date dateOfBooking;

    public String getDreamerId() {
        return dreamerId;
    }
    public void setDreamerId(String dreamerId) {
        this.dreamerId = dreamerId;
    }
    public String getPlanet() {
        return planet;
    }
    public void setPlanet(String planet) {
        this.planet = planet;
    }
    public String getPlanetThumbnail() {
        return planetThumbnail;
    }
    public void setPlanetThumbnail(String planetThumbnail) {
        this.planetThumbnail = planetThumbnail;
    }
    public Integer getNumberOfPax() {
        return numberOfPax;
    }
    public void setNumberOfPax(Integer numberOfPax) {
        this.numberOfPax = numberOfPax;
    }
    public Date getTravelDate() {
        return travelDate;
    }
    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }
    public String getStringDate() {
        return stringDate;
    }
    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }
    public Double getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
    public Date getDateOfBooking() {
        return dateOfBooking;
    }
    public void setDateOfBooking(Date dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public static Booking create(JsonObject jsonBooking) {
        Booking booking = new Booking();
        booking.setPlanet(jsonBooking.getString("planet"));
        booking.setPlanetThumbnail(jsonBooking.getString("planetThumbnail"));
        booking.setNumberOfPax(jsonBooking.getInt("numberOfPax"));
        booking.setStringDate(jsonBooking.getString("travelDate"));
        booking.setTotalCost(Double.parseDouble(jsonBooking.getJsonNumber("totalCost").toString()));
        return booking;
    }

    public static Booking create(SqlRowSet srs) {
        Booking booking = new Booking();
        booking.setDreamerId(srs.getString("dreamer_id"));
        booking.setPlanet(srs.getString("planet"));
        booking.setNumberOfPax(srs.getInt("number_of_pax"));
        booking.setStringDate(srs.getString("travel_date"));
        return booking;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                    .add("planet", planet)
                    .add("planetThumbnail", planetThumbnail)
                    .add("numberOfPax", numberOfPax)
                    .add("travelDate", travelDate.toString())
                    .add("totalCost", totalCost)
                    .build();
    }
}
