package sg.edu.nus.iss.server.models;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;

import org.springframework.jdbc.support.rowset.SqlRowSet;

// @Entity
public class Dreamer implements Serializable {
    
    // Mark the primary key (Auto-generated and the Primary key column is not null and not updatable)
    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // @Column(nullable = false, updatable = false)
    private int id;
    private String dreamerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profileImageUrl;
    // private Blob profileImage;
    private String dateOfBirth;
    private String gender;
    private Date lastLoginDateDisplay;
    private Date lastLoginDate;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    // Default constructor
    public Dreamer() {}
    
    // Constructor with all parameters
    public Dreamer(int id, String dreamerId, String firstName, String lastName, String email, String password, String profileImageUrl,
            String dateOfBirth, String gender, Date lastLoginDateDisplay, Date lastLoginDate, Date joinDate, String role, String[] authorities,
            boolean isActive, boolean isNotLocked) {
        this.id = id;
        this.dreamerId = dreamerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        // this.profileImage = profileImage;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.lastLoginDate = lastLoginDate;
        this.joinDate = joinDate;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
    }

    // Generate Getter and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDreamerId() {
        return dreamerId;
    }

    public void setDreamerId(String dreamerId) {
        this.dreamerId = dreamerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public Date getLastLoginDateDisplay() {
        return lastLoginDateDisplay;
    }

    public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
        this.lastLoginDateDisplay = lastLoginDateDisplay;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsNotLocked() {
        return isNotLocked;
    }

    public void setIsNotLocked(boolean isNotLocked) {
        this.isNotLocked = isNotLocked;
    }

    // public Blob getProfileImage() {
    //     return profileImage;
    // }

    // public void setProfileImage(Blob profileImage) {
    //     this.profileImage = profileImage;
    // }

    // Create Dreamer object from SqlRowSet
    public static Dreamer create(SqlRowSet srs) {
        Dreamer dreamer = new Dreamer();
        dreamer.setId(srs.getInt("id"));
        dreamer.setDreamerId(srs.getString("dreamer_id"));
        dreamer.setFirstName(srs.getString("first_name"));
        dreamer.setLastName(srs.getString("last_name"));
        dreamer.setDateOfBirth(srs.getString("date_of_birth"));
        dreamer.setGender(srs.getString("gender"));
        dreamer.setEmail(srs.getString("email"));
        dreamer.setPassword(srs.getString("password"));
        dreamer.setProfileImageUrl(srs.getString("profile_image_url"));
        // if (srs.getObject("profile_image") != null) {
        //     dreamer.setProfileImage((Blob)srs.getObject("profile_image"));
        // }
        if (srs.getDate("last_login") != null) {
            dreamer.setLastLoginDate(new Date(srs.getDate("last_login").getTime()));
        }
        if (srs.getDate("last_login_display") != null) {
            dreamer.setLastLoginDateDisplay(new Date(srs.getDate("last_login_display").getTime()));
        }
        dreamer.setJoinDate(new Date(srs.getDate("join_date").getTime()));
        dreamer.setRole(srs.getString("role"));
        dreamer.setIsActive(srs.getBoolean("active"));
        dreamer.setIsNotLocked(srs.getBoolean("not_locked"));
        return dreamer;
    }
    
}
