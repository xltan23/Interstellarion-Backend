package sg.edu.nus.iss.server.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Planet {
    
    // Planet Attributes
    private String name;
    private Double mass; // In Jupiter's mass
    private Double radius; // In Jupiter's radius (Missing info for distant planets)
    private Double period; // In Earth Days
    private Double semi_major_axis; // In Astronomical Units
    private Integer temperature; // In Kelvin (Missing info for distant planets)
    private Double distance_light_year; // In Light years
    private Double host_star_mass; // In Kilogram
    private Double host_star_temperature; // In Kelvin
    private Double gravity; // In m/s^2
    private Double solar_insolation; // In W/m^2 (Estimation of planet's temperature)
    private String star_type; // Take into account luminosity (from mass) and temperature
    private Double travel_time; // In Earth Years
    private Double cost; // In USD
    private String description; // To be inserted by admin
    private String thumbnailUrl; // To be inserted by admin
    private String coverUrl; // To be inserted by admin

    // Generate Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getPeriod() {
        return period;
    }

    public void setPeriod(Double period) {
        this.period = period;
    }

    public Double getSemi_major_axis() {
        return semi_major_axis;
    }

    public void setSemi_major_axis(Double semi_major_axis) {
        this.semi_major_axis = semi_major_axis;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Double getDistance_light_year() {
        return distance_light_year;
    }

    public void setDistance_light_year(Double distance_light_year) {
        this.distance_light_year = distance_light_year;
    }

    public Double getHost_star_mass() {
        return host_star_mass;
    }

    public void setHost_star_mass(Double host_star_mass) {
        this.host_star_mass = host_star_mass;
    }

    public Double getHost_star_temperature() {
        return host_star_temperature;
    }

    public void setHost_star_temperature(Double host_star_temperature) {
        this.host_star_temperature = host_star_temperature;
    }

    public Double getGravity() {
        return gravity;
    }

    public void setGravity(Double gravity) {
        this.gravity = gravity;
    }

    public Double getSolar_insolation() {
        return solar_insolation;
    }

    public void setSolar_insolation(Double solar_insolation) {
        this.solar_insolation = solar_insolation;
    }

    public String getStar_type() {
        return star_type;
    }

    public void setStar_type(String star_type) {
        this.star_type = star_type;
    }

    public Double getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(Double travel_time) {
        this.travel_time = travel_time;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    // Convert JsonObject to Planet object (Used when converting API data to MySQL data)
    public static Planet createPlanet(JsonObject jsonPlanet) {
        Planet planet = new Planet();
        planet.setName(jsonPlanet.getString("name"));
        planet.setMass(Double.parseDouble(isNotNull(jsonPlanet.get("mass").toString()) ? jsonPlanet.getJsonNumber("mass").toString() : "-1"));
        planet.setRadius(Double.parseDouble(isNotNull(jsonPlanet.get("radius").toString()) ? jsonPlanet.getJsonNumber("radius").toString() : "-1"));
        planet.setPeriod(Double.parseDouble(isNotNull(jsonPlanet.get("period").toString()) ? jsonPlanet.getJsonNumber("period").toString() : "-1"));
        planet.setSemi_major_axis(Double.parseDouble(isNotNull(jsonPlanet.get("semi_major_axis").toString()) ? jsonPlanet.getJsonNumber("semi_major_axis").toString() : "-1"));
        planet.setTemperature(isNotNull(jsonPlanet.get("temperature").toString()) ? jsonPlanet.getInt("temperature") : -1);
        planet.setDistance_light_year(Double.parseDouble(isNotNull(jsonPlanet.get("distance_light_year").toString()) ? jsonPlanet.getJsonNumber("distance_light_year").toString() : "-1"));
        planet.setHost_star_mass(Double.parseDouble(isNotNull(jsonPlanet.get("host_star_mass").toString()) ? jsonPlanet.getJsonNumber("host_star_mass").toString() : "-1"));
        planet.setHost_star_temperature(Double.parseDouble(isNotNull(jsonPlanet.get("host_star_temperature").toString()) ? jsonPlanet.getJsonNumber("host_star_temperature").toString() : "-1"));
        return planet;
    }

    // Convert from SqlRowSet to Planet object (Used when retrieving planet from the database)
    public static Planet createPlanet(SqlRowSet srs) {
        Planet planet = new Planet();
        planet.setName(srs.getString("name"));
        planet.setMass(srs.getDouble("mass"));
        planet.setRadius(srs.getDouble("radius"));
        planet.setPeriod(srs.getDouble("period"));
        planet.setSemi_major_axis(srs.getDouble("semi_major_axis"));
        planet.setTemperature(srs.getInt("temperature"));
        planet.setDistance_light_year(srs.getDouble("distance_light_year"));
        planet.setHost_star_mass(srs.getDouble("host_star_mass"));
        planet.setHost_star_temperature(srs.getDouble("host_star_temperature"));
        planet.setGravity(srs.getDouble("gravity"));
        planet.setSolar_insolation(srs.getDouble("solar_insolation"));
        planet.setStar_type(srs.getString("star_type"));
        planet.setTravel_time(srs.getDouble("travel_time"));
        planet.setCost(srs.getDouble("cost"));
        planet.setDescription(srs.getString("description"));
        planet.setThumbnailUrl(srs.getString("thumbnailUrl"));
        planet.setCoverUrl(srs.getString("coverUrl"));
        return planet;
    }

    // Convert Planet object to JsonObject (Only used for testing API)
    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                    .add("name", name)
                    .add("mass", mass)
                    .add("radius", radius)
                    .add("period", period)
                    .add("semi_major_axis", semi_major_axis)
                    .add("temperature", temperature)
                    .add("distance_light_year", distance_light_year)
                    .add("host_star_mass", host_star_mass)
                    .add("host_star_temperature", host_star_temperature)
                    .build();
    }

    private static boolean isNotNull(String jsonValue) {
        return !jsonValue.equals("null");
    }
}
