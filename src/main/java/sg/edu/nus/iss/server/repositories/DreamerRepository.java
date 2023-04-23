package sg.edu.nus.iss.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.server.models.Dreamer;

import static sg.edu.nus.iss.server.repositories.Queries.*;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Repository
public class DreamerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Query MySQL to insert new Dreamer
    public boolean register(Dreamer dreamer) {
        System.out.println("Registering dreamer...");
        // Insert new Dreamer in DREAMERS
        int dreamerInserted = jdbcTemplate.update(SQL_INSERT_NEW_DREAMER, dreamer.getDreamerId(), dreamer.getFirstName(),
            dreamer.getLastName(), dreamer.getDateOfBirth(), dreamer.getGender(), dreamer.getEmail(), dreamer.getPassword(),
            dreamer.getProfileImageUrl(), dreamer.getJoinDate(), dreamer.getRole(), dreamer.getIsActive(), dreamer.getIsNotLocked());
        // Insert new Dreamer in IMAGES
        int imageInserted = jdbcTemplate.update(SQL_INSERT_DREAMER_PROFILE_PIC, dreamer.getDreamerId());
        return dreamerInserted > 0 && imageInserted > 0;
    }

    // Query MySQL to find Dreamer
    public Dreamer findDreamerByEmail(String email) {
        System.out.println("Finding dreamer by email...");
        // Select existing Dreamer in DREAMERS
        SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_FIND_DREAMER_BY_EMAIL, email);
        List<Dreamer> dreamers = new LinkedList<>();
        // Should return only 1 Dreamer
        while (srs.next()) {
            dreamers.add(Dreamer.create(srs));
        }
        if (dreamers.size() == 0) {
            return null;
        }
        return dreamers.get(0);
    }

    // Query MySQL to find Dreamer
    public Dreamer findDreamerById(String dreamerId) {
        System.out.println("Finding dreamer by Dreamer ID...");
        SqlRowSet srs = jdbcTemplate.queryForRowSet(SQL_FIND_DREAMER_BY_ID, dreamerId);
        List<Dreamer> dreamers = new LinkedList<>();
        // Should return only 1 Dreamer
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
        // Update existing Dreamer in DREAMERS
        int dreamerUpdated = jdbcTemplate.update(SQL_UPDATE_DREAMER_LOGIN, dreamer.getLastLoginDate(), dreamer.getLastLoginDateDisplay(), dreamer.getEmail());
        return dreamerUpdated > 0;
    }

    // Query MySQL to update Password of dreamer
    public boolean updatePassword(Dreamer dreamer) {
        System.out.println("Changing password...");
        // Update existing Dreamer in DREAMERS
        int passwordChanged = jdbcTemplate.update(SQL_UPDATE_DREAMER_PASSWORD, dreamer.getPassword(), dreamer.getEmail());
        return passwordChanged > 0;
    }

    // Query MySQL to update Profile of dreamer
    public boolean updateProfile(Dreamer dreamer, Blob blob) {
        System.out.println("Updating profile of dreamer...");
        int dreamerUpdated = jdbcTemplate.update(SQL_UPDATE_DREAMER_PROFILE, dreamer.getFirstName(), dreamer.getLastName(), dreamer.getEmail());
        int imageUpdated = jdbcTemplate.update(SQL_UPDATE_DREAMER_PROFILE_PIC, blob, dreamer.getDreamerId());
        return dreamerUpdated > 0 && imageUpdated > 0;
    }

    // Query MySQL to delete dreamer
    public boolean delete(Dreamer dreamer) {
        System.out.println("Deleting dreamer...");
        int dreamerDeleted = jdbcTemplate.update(SQL_DELETE_DREAMER, "deleted", "deleted", "deleted", "deleted", "deleted",
            "deleted", "deleted", "deleted", false, false, dreamer.getEmail());
        return dreamerDeleted > 0;
    }

    // Query to get Blob from database
    public Blob getProfileImage(String dreamerId) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dreamerdb", "dreamuser", "Ddb@123");
        PreparedStatement ps = con.prepareStatement(SQL_SELECT_PROFILE_IMAGE);
        ResultSet rs = ps.executeQuery();
        Blob blob = null;
        while(rs.next()) {
            if (rs.getString("dreamer_id").equals(dreamerId)) {
                blob = rs.getBlob("profile_image");
            }
        }
        return blob;
    }
}
