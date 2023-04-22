package sg.edu.nus.iss.server.repositories;

public class Queries {
    
    // DREAMERS REPOSITORY
    public static final String SQL_INSERT_NEW_DREAMER = "INSERT INTO dreamers (dreamer_id, first_name, last_name, date_of_birth, gender, email, password, profile_image_url, join_date, role, active, not_locked) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String SQL_FIND_DREAMER_BY_EMAIL = "SELECT * FROM dreamers WHERE email = ?";
    public static final String SQL_UPDATE_DREAMER_LOGIN = "UPDATE dreamers SET last_login = ?, last_login_display = ? WHERE email = ?";
    public static final String SQL_UPDATE_DREAMER_PASSWORD = "UPDATE dreamers SET password = ? WHERE email = ?";
    public static final String SQL_UPDATE_DREAMER_PROFILE = "UPDATE dreamers SET first_name = ?, last_name = ? WHERE email = ?";
    public static final String SQL_DELETE_DREAMER = "UPDATE dreamers SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, email = ?, password = ?, profile_image_url = ?, role = ?, active = ?, not_locked = ? WHERE email = ?";
    public static final String SQL_SELECT_PROFILE_IMAGE = "SELECT * FROM images";

    // IMAGES REPOSITORY
    public static final String SQL_INSERT_DREAMER_PROFILE_PIC = "INSERT INTO images (dreamer_id) VALUES (?)";
    public static final String SQL_UPDATE_DREAMER_PROFILE_PIC = "UPDATE images SET profile_image = ? WHERE dreamer_id = ?";

    // PLANETS REPOSITORY
    public static final String SQL_SELECT_PLANET = "SELECT * FROM planets WHERE name = ?";
    public static final String SQL_INSERT_NEW_PLANET = "INSERT INTO planets (name, mass, radius, period, semi_major_axis, temperature, distance_light_year, host_star_mass, host_star_temperature, gravity, star_type, travel_time, cost, description, thumbnailUrl, coverUrl) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String SQL_SELECT_DEFAULT_PLANETS = "SELECT * FROM planets WHERE name in (\"Mercury\",\"Venus\",\"Earth\",\"Mars\",\"Jupiter\",\"Saturn\",\"Uranus\",\"Neptune\",\"Pluto\")";
    // CALLED BY ADMIN
    public static final String SQL_UPDATE_EXISTING_PLANET = "UPDATE planets SET description = ?, thumbnailUrl = ?, coverUrl = ? WHERE name = ?";
    public static final String SQL_INSERT_BACKGROUND = "INSERT INTO background (title, background) VALUES (?,?)";
}
