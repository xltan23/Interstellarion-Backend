package sg.edu.nus.iss.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import sg.edu.nus.iss.server.models.Planet;
import sg.edu.nus.iss.server.services.PlanetService;

@RestController
@RequestMapping(path = "/planets")
@CrossOrigin(origins = "*")
public class PlanetController {
    
    @Autowired
    private PlanetService planetSvc;

    @GetMapping("{planet}")
    public ResponseEntity<List<Planet>> getPlanetsByName(@PathVariable String planet) {
        List<Planet> planetList = planetSvc.getPlanetsFromName(planet);
        return new ResponseEntity<>(planetList, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Planet>> getAllPlanets() {
        List<Planet> planetList = planetSvc.getAllPlanets();
        return new ResponseEntity<>(planetList, HttpStatus.OK);
    }

    // @GetMapping("{planet}")
    // public ResponseEntity<String> getPlanetsByName(@PathVariable String planet) {
    //     List<Planet> planetList = planetSvc.getPlanetsFromName(planet);
    //     JsonArrayBuilder jab = Json.createArrayBuilder();
    //     for (int i = 0; i < planetList.size(); i++) {
    //         jab.add(planetList.get(i).toJSON());
    //     }
        
    //     return new ResponseEntity<>(jab.build().toString(), HttpStatus.OK);
    // }

    // @GetMapping("/all")
    // public ResponseEntity<String> getPlanetsByName() {
    //     List<Planet> planetList = planetSvc.getAllPlanets();
    //     JsonArrayBuilder jab = Json.createArrayBuilder();
    //     for (int i = 0; i < planetList.size(); i++) {
    //         jab.add(planetList.get(i).toJSON());
    //     }
        
    //     return new ResponseEntity<>(jab.build().toString(), HttpStatus.OK);
    // }
}
