package sg.edu.nus.iss.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Planet {
    
    private String name;
    private Double mass;
    private Double radius;
    private Double period;
    private Double semi_major_axis;
    private Integer temperature;
    private Double distance_light_year;
    private Double host_star_mass;
    private Double host_star_temperature;

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

    // Convert JsonObject to Planet object
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
