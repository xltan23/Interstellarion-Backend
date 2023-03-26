package sg.edu.nus.iss.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.server.models.Dreamer;

import static sg.edu.nus.iss.server.repositories.Queries.*;

import java.util.LinkedList;
import java.util.List;

@Repository
public class DreamerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Query MySQL to insert dreamer
    public boolean register(Dreamer dreamer) {
        System.out.println("Registering dreamer...");
        int rowsInserted = jdbcTemplate.update(SQL_INSERT_NEW_DREAMER, dreamer.getDreamerId(), dreamer.getFirstName(),
            dreamer.getLastName(), dreamer.getDateOfBirth(), dreamer.getGender(), dreamer.getEmail(), dreamer.getPassword(),
            dreamer.getProfileImageUrl(), dreamer.getJoinDate(), dreamer.getRole(), dreamer.getIsActive(), dreamer.getIsNotLocked());
        return rowsInserted > 0;
    }

    // Query MySQL to find dreamer
    public Dreamer findDreamerByEmail(String email) {
        System.out.println("Finding dreamer by email...");
        SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_FIND_DREAMER_BY_EMAIL, email);
        List<Dreamer> dreamers = new LinkedList<>();
        while (srs.next()) {
            dreamers.add(Dreamer.create(srs));
        }
        if (dreamers.size() == 0) {
            return null;
        }
        return dreamers.get(0);
    }

    // Query MySQL to update Last Login of dreamer 
    public boolean updateLastLogin(Dreamer dreamer) {
        System.out.println("Updating dreamer...");
        int rowsInserted = jdbcTemplate.update(SQL_UPDATE_DREAMER_LOGIN, dreamer.getLastLoginDate(), dreamer.getLastLoginDateDisplay(), dreamer.getEmail());
        return rowsInserted > 0;
    }

    // Query MySQL to update Password of dreamer
    public boolean updatePassword(Dreamer dreamer) {
        System.out.println("Changing password...");
        int rowsInserted = jdbcTemplate.update(SQL_UPDATE_DREAMER_PASSWORD, dreamer.getPassword(), dreamer.getEmail());
        return rowsInserted > 0;
    }

    // Query MySQL to update Profile of dreamer
    public boolean updateProfile(Dreamer dreamer) {
        System.out.println("Updating profile of dreamer...");
        int rowsInserted = jdbcTemplate.update(SQL_UPDATE_DREAMER_PROFILE, dreamer.getFirstName(), dreamer.getLastName(), dreamer.getProfileImageUrl(), dreamer.getEmail());
        return rowsInserted > 0;
    }

    // Query MySQL to delete dreamer
    public boolean delete(Dreamer dreamer) {
        System.out.println("Deleting dreamer...");
        int rowsInserted = jdbcTemplate.update(SQL_DELETE_DREAMER, dreamer.getEmail());
        return rowsInserted > 0;
    }
}
