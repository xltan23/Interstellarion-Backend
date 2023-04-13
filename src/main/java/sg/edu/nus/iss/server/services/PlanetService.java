package sg.edu.nus.iss.server.services;

import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import sg.edu.nus.iss.server.models.Planet;

@Service
public class PlanetService {

    @Value("${API_KEY}")
    private String ninjaKey;

    public static final String URL = "https://api.api-ninjas.com/v1/planets";
    
    // Get specific planet by name
    public List<Planet> getPlanetsFromName(String name) {

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
            planetList.add(Planet.createPlanet(jsonPlanet));
        }
        // System.out.println(planetList.size());
        return planetList;
    }

    // Get all planets (By default)
    public List<Planet> getAllPlanets() {

        String url = UriComponentsBuilder.fromUriString(URL)
                        .queryParam("min_period", 365)
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
            planetList.add(Planet.createPlanet(jsonPlanet));
        }
        System.out.println(planetList.size());
        return planetList;
    }
}
