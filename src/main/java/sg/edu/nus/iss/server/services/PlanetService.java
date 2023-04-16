package sg.edu.nus.iss.server.services;

import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.server.models.PlanetSearch;
import sg.edu.nus.iss.server.models.Planet;
import sg.edu.nus.iss.server.repositories.PlanetRepository;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepo;

    @Value("${API_KEY}")
    private String ninjaKey;

    public static final String URL = "https://api.api-ninjas.com/v1/planets";
    
    // Get specific planet by name
    public List<Planet> getPlanetsByName(String name) {

        // Attempt to retrieve from database (Will be used to build their individual webpages)
        List<Planet> planetDB = new LinkedList<>();
        planetDB = planetRepo.getPlanetsByName(name);
        if (!planetDB.isEmpty()) {
            return planetDB;
        }

        // If not already in database, search from API and pass new Planet into database
        String url = UriComponentsBuilder.fromUriString(URL)
                        .queryParam("name", name)
                        .toUriString();
        
        RequestEntity<Void> req = RequestEntity.get(url.replaceAll("%20", " "))
                                        .header("X-Api-Key", ninjaKey)
                                        .build();

        String payload = "";

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
            payload = resp.getBody();
            System.out.println(payload);

        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return Collections.emptyList();
        }               

        StringReader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonArray planetArray = jsonReader.readArray();
        List<Planet> planetList = new LinkedList<>();
        for (int i = 0; i < planetArray.size(); i++) {
            JsonObject jsonPlanet = planetArray.getJsonObject(i);
            Planet planet = Planet.createPlanet(jsonPlanet);
            planet.setGravity(calculateGravity(planet.getMass()));
            planet.setSolar_insolation(calculateInsolation(planet.getSemi_major_axis(), planet.getHost_star_temperature()));
            planet.setStar_type(determineStarType(planet.getHost_star_mass(), planet.getHost_star_temperature()));
            planet.setTravel_time(calculateTravelTime(planet.getDistance_light_year()));
            planet.setCost(calculateTotalCost(planet.getGravity(), planet.getSolar_insolation(), planet.getStar_type(), planet.getTravel_time()));
            planet.setDescription("");
            planet.setThumbnailUrl("");
            planet.setCoverUrl("");
            if (planetRepo.getPlanetsByName(planet.getName()).isEmpty()) {
               planetRepo.insertPlanet(planet); 
            }
            planetList.add(planet);
        }

        return planetList;
    }

    public List<Planet> getDefaultPlanets() {
        return planetRepo.getDefaultPlanets();
    }

    // Get Default planets
    public List<Planet> getFilteredPlanets(PlanetSearch planetSearch) {

        String url = UriComponentsBuilder.fromUriString(URL)
                        .queryParam("min_mass", planetSearch.getMin_mass())
                        .queryParam("max_mass", planetSearch.getMax_mass())
                        .queryParam("min_semi_major_axis", planetSearch.getMin_semi_major_axis())
                        .queryParam("max_semi_major_axis", planetSearch.getMax_semi_major_axis())
                        .queryParam("max_distance_light_year", planetSearch.getMax_distance_light_year())
                        .toUriString();
        
        RequestEntity<Void> req = RequestEntity.get(url)
                                            .header("X-Api-Key", ninjaKey)
                                            .build();
        
        String payload = "";

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
            payload = resp.getBody();
            System.out.println(payload);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return Collections.emptyList();
        }

        StringReader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonArray planetArray = jsonReader.readArray();
        List<Planet> planetList = new LinkedList<>();
        for (int i = 0; i < planetArray.size(); i++) {
            JsonObject jsonPlanet = planetArray.getJsonObject(i);
            Planet planet = Planet.createPlanet(jsonPlanet);
            planet.setGravity(calculateGravity(planet.getMass()));
            planet.setSolar_insolation(calculateInsolation(planet.getSemi_major_axis(), planet.getHost_star_temperature()));
            planet.setStar_type(determineStarType(planet.getHost_star_mass(), planet.getHost_star_temperature()));
            planet.setTravel_time(calculateTravelTime(planet.getDistance_light_year()));
            planet.setCost(calculateTotalCost(planet.getGravity(), planet.getSolar_insolation(), planet.getStar_type(), planet.getTravel_time()));
            planet.setDescription("");
            planet.setThumbnailUrl("");
            planet.setCoverUrl("");
            planetList.add(planet);
        }
        System.out.println(planetList.size());
        return planetList;
    }

    private Double calculateGravity(Double mass) {
        return -1.0;
    }

    private Double calculateInsolation(Double semi_major_axis, Double host_star_temperature) {
        return -1.0;
    }

    private String determineStarType(Double mass, Double temperature) {
        return "";
    }

    private Double calculateTravelTime(Double distance_light_year) {
        return -1.0;
    }
   
    private Double calculateTotalCost(Double gravity, Double solar_insolation, String star_type, Double travel_time) {
        return -1.0;
    }
}
