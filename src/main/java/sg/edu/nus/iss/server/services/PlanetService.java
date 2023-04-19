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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.server.models.PlanetSearch;
import sg.edu.nus.iss.server.models.Apod;
import sg.edu.nus.iss.server.models.Planet;
import sg.edu.nus.iss.server.repositories.ImageRepository;
import sg.edu.nus.iss.server.repositories.PlanetRepository;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepo;

    @Autowired
    private ImageRepository imageRepo;

    @Value("${NINJA_API_KEY}")
    private String ninjaKey;

    @Value("${NASA_API_KEY}")
    private String nasaKey;

    public static final String URL = "https://api.api-ninjas.com/v1/planets";
    public static final String NASAURL = "https://api.nasa.gov/planetary/apod";
    
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
            planet.setGravity(rounding(calculateGravity(planet.getMass(), planet.getRadius())));
            planet.setStar_type(determineStarType(planet.getHost_star_mass(), planet.getHost_star_temperature()));
            planet.setTravel_time(rounding(calculateTravelTime(planet.getDistance_light_year())));
            planet.setCost(rounding(calculateTotalCost(planet.getGravity(), planet.getSemi_major_axis(), planet.getStar_type(), planet.getTravel_time())));
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

    // Get Default planets
    public List<Planet> getDefaultPlanets() {
        return planetRepo.getDefaultPlanets();
    }

    // Get Filtered planets
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
            planet.setGravity(rounding(calculateGravity(planet.getMass(), planet.getRadius())));
            planet.setStar_type(determineStarType(planet.getHost_star_mass(), planet.getHost_star_temperature()));
            planet.setTravel_time(rounding(calculateTravelTime(planet.getDistance_light_year())));
            planet.setCost(rounding(calculateTotalCost(planet.getGravity(), planet.getSemi_major_axis(), planet.getStar_type(), planet.getTravel_time())));
            planet.setDescription("");
            planet.setThumbnailUrl("");
            planet.setCoverUrl("");
            planetList.add(planet);
        }
        System.out.println(planetList.size());
        return planetList;
    }

    // Get list of planets
    public boolean updatePlanet(String name, MultipartFile thumbnail, MultipartFile cover, String description) {
        Planet planet = new Planet();
        planet.setName(name);
        planet.setDescription(description);
        String thumbnailKey = "%sThumbnail".formatted(name);
        String coverKey = "%sCover".formatted(name);
        planet.setThumbnailUrl(imageRepo.uploadImage(thumbnailKey, thumbnail));
        planet.setCoverUrl(imageRepo.uploadImage(coverKey, cover));
        return planetRepo.updatePlanet(planet);
    }

    // Get APOD from NASA API
    public Apod getApod() {
        String nasaUrl = UriComponentsBuilder.fromUriString(NASAURL)
                            .queryParam("api_key", nasaKey)
                            .toUriString();
        RequestEntity<Void> req = RequestEntity.get(nasaUrl).build();
        String payload = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
            payload = resp.getBody();
            System.out.println(payload);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            Apod emptyApod = new Apod();
            return emptyApod;
        }
        StringReader sr = new StringReader(payload);
        JsonReader jr = Json.createReader(sr);
        JsonObject jsonApod = jr.readObject();
        return Apod.create(jsonApod);
    }

    private Double calculateGravity(Double mass, Double radius) {
        Double gravitationalConstant = 6.6743*Math.pow(10, -11);
        Double jupiterMass = 1.898*Math.pow(10, 27);
        Double jupiterRadius = 6.9911*Math.pow(10, 7);
        // Using Polynomial Equation obtain by graphing mass vs radius: 
        // Radius = 1.016*mass**0.3748
        if (radius == -1) {
            if (mass <= 0.005) {
                radius = 0.1;
            } 
            if (mass > 0.005 && mass <= 0.05) {
                radius = 0.3;
            }
            if (mass > 0.05 && mass <= 0.5) {
                radius = 0.7;
            }
            // More mass start to shrink planet (compression from its own gravity)
            if (mass > 0.5 && mass <= 5) {
                radius = 1.0;
            }
            if (mass > 5) {
                radius = 1.2;
            }
        }
        Double gravity = gravitationalConstant*mass*jupiterMass/Math.pow(radius*jupiterRadius, 2);
        return gravity;
    }

    private String determineStarType(Double mass, Double temperature) {
        // Based on Hertzsprung-Russell diagram
        if (mass == -1 || temperature == -1) {
            return "Unknown";
        }
        // If mass is > 10Msun and Temperature more than 20000K
        if (mass > 10 && temperature >= 10000) {
            return "Blue Supergiant";
        }
        // If mass is > 10Msun and Temperature less than 4100K
        if (mass > 10 && temperature <= 5000) {
            return "Red Supergiant";
        }
        if (mass < 8 && temperature >= 10000) {
            return "Blue Giant";
        }
        // If mass is ~1.5Msun - 8Msun and Temperature less than 6000K
        if (mass < 8 && temperature <= 4000) {
            return "Red Giant";
        }
        // If mass is ~0.15Msun - 1.2Msun and Temperature more than 9000K
        if (mass < 1.2 && temperature >= 10000) {
            return "White Dwarf";
        }
        return "Main-Sequence Star";
    }

    private Double calculateTravelTime(Double distance_light_year) {
        // Fastest Spacecraft at 535,000 km/s
        // Extrapolating to futuristic speed or possible wormholes
        Double travel_time = distance_light_year*100;
        return travel_time;
    }
   
    private Double calculateTotalCost(Double gravity, Double semi_major_axis, String star_type, Double travel_time) {
        // Travel Time sets the base cost
        Double cost = 999.0;
        // Within Solar System
        if (travel_time <= 1) {
            cost += 0.0;
        }
        // Outside Solar System (Nearby ~1-5 ly)
        if (travel_time > 1 && travel_time <= 200) {
            cost += travel_time*8;
        }
        // Outside Solar System (Moderate ~5-20 ly)
        if (travel_time > 200 && travel_time <= 2000) {
            cost += travel_time*20;
        }
        // Outside Solar System (Far ~20-50 ly)
        if (travel_time > 2000 && travel_time <= 5000) {
            cost += travel_time*35;
        }
        // Outside Solar System (Far ~50-100 ly)
        if (travel_time > 5000 && travel_time < 10000) {
            cost += travel_time*50;
        }

        // Low Gravity (Require mobility aid and may lead to muscle-related health problems)
        if (gravity <= 6) {
            cost *= 1.1;
        }
        // Optimal Gravity
        if (gravity > 6 && gravity <= 11) {
            cost *= 1.5;
        }
        // High Gravity 
        if (gravity > 11) {
            cost *= 0.8;
        }

        // Distance of planet from host star
        // Near Host Star: More technology required to mitigate heat of star
        if (semi_major_axis <= 0.5) {
            cost *= 1.5;
        }
        // Goldilock's Zone
        if (semi_major_axis > 0.5 && semi_major_axis <= 2) {
            cost *= 1.0;
        } 
        // Heating technology required
        if (semi_major_axis > 2 && semi_major_axis <= 6) {
            cost *= 1.3;
        }
        // Advanced heating technology required
        if (semi_major_axis > 6 && semi_major_axis <= 20) {
            cost *= 1.5;
        }
        // Advanced heating technology with lighting required
        if (semi_major_axis > 20) {
            cost *= 1.6;
        }

        // Check star type (Subject to the beauty and rarity)
        if (star_type.equals("Main-Sequence Star")) {
            cost *= 1.0;
        }
        if (star_type.equals("Blue Supergiant")) {
            cost *= 1.3;
        }
        if (star_type.equals("Red Supergiant")) {
            cost *= 1.3;
        }
        if (star_type.equals("Blue Giant")) {
            cost *= 1.2;
        }
        if (star_type.equals("Red Giant")) {
            cost *= 1.1;
        }
        if (star_type.equals("White Dwarf")) {
            cost *= 1.3;
        }

        return cost;
    }

    private Double rounding(Double value) {
        if (value < 0.001) {
            Double lowRounded = (double) Math.round(value*100000)/100000;
            return lowRounded;
        }
        Double rounded = (double) Math.round(value*100)/100;
        return rounded;
    }
}
