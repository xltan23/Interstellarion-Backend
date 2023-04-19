package sg.edu.nus.iss.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.server.models.PlanetSearch;
import sg.edu.nus.iss.server.security.HttpResponse;
import sg.edu.nus.iss.server.models.Apod;
import sg.edu.nus.iss.server.models.Planet;
import sg.edu.nus.iss.server.services.PlanetService;

@RestController
@RequestMapping(path = "/planets")
@CrossOrigin(origins = "*")
public class PlanetController {
    
    @Autowired
    private PlanetService planetSvc;

    @GetMapping("/apod")
    public ResponseEntity<Apod> getApod() {
        Apod apod = planetSvc.getApod();
        return new ResponseEntity<>(apod, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Planet>> getPlanetsByFilter(@RequestBody PlanetSearch planetSearch) {
            List<Planet> planetList = planetSvc.getFilteredPlanets(planetSearch);
            return new ResponseEntity<>(planetList, HttpStatus.OK);
    }

    @GetMapping("{planet}")
    public ResponseEntity<List<Planet>> getPlanetsByName(@PathVariable String planet) {
        List<Planet> planetList = planetSvc.getPlanetsByName(planet);
        return new ResponseEntity<>(planetList, HttpStatus.OK);
    }

    @GetMapping("/default")
    public ResponseEntity<List<Planet>> getAllPlanets() {
        List<Planet> planetList = planetSvc.getDefaultPlanets();
        return new ResponseEntity<>(planetList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updatePlanet(@RequestPart String name, @RequestPart MultipartFile thumbnail, @RequestPart MultipartFile cover, @RequestPart String description) {
        String message = "Update Unsuccessful.";
        Boolean updateSuccess = planetSvc.updatePlanet(name, thumbnail, cover, description);
        if (updateSuccess) {
            message = "Update Successful!";
        }
        return response(HttpStatus.OK, message);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(), message), httpStatus);
    }
}
