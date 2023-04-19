package sg.edu.nus.iss.server.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.server.models.Planet;

import static sg.edu.nus.iss.server.repositories.Queries.*;

@Repository
public class PlanetRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Planet> getPlanetsByName(String name) {
        List<Planet> planetList = new LinkedList<>();
        SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_SELECT_PLANET, name);
        while(srs.next()) {
            planetList.add(Planet.createPlanet(srs));
        }
        return planetList;
    }

    public List<Planet> getDefaultPlanets() {
        List<Planet> planetList = new LinkedList<>();
        SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_SELECT_DEFAULT_PLANETS);
        while(srs.next()) {
            planetList.add(Planet.createPlanet(srs));
        }
        return planetList;
    }

    public boolean insertPlanet(Planet planet) {
        int planetsInserted = jdbcTemplate.update(SQL_INSERT_NEW_PLANET, planet.getName(), planet.getMass(), planet.getRadius(), planet.getPeriod(), 
            planet.getSemi_major_axis(), planet.getTemperature(), planet.getDistance_light_year(), planet.getHost_star_mass(), 
            planet.getHost_star_temperature(), planet.getGravity(), planet.getStar_type(), planet.getTravel_time(), planet.getCost(), 
            planet.getDescription(), planet.getThumbnailUrl(), planet.getCoverUrl());
        return planetsInserted > 0;
    }

    public boolean updatePlanet(Planet planet) {
        int planetUpdated = jdbcTemplate.update(SQL_UPDATE_EXISTING_PLANET, planet.getDescription(), planet.getThumbnailUrl(), planet.getCoverUrl(), planet.getName());
        return planetUpdated > 0;
    }
}
