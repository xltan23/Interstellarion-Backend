package sg.edu.nus.iss.server.repositories;

public class Queries {
    
    public static final String SQL_INSERT_NEW_DREAMER = "INSERT INTO dreamers (dreamer_id, first_name, last_name, date_of_birth, gender, email, password, profile_image_url, join_date, role, active, not_locked) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String SQL_INSERT_DREAMER_PROFILE_PIC = "INSERT INTO images (dreamer_id) VALUES (?)";
    public static final String SQL_FIND_DREAMER_BY_EMAIL = "SELECT * FROM dreamers WHERE email = ?";
    public static final String SQL_UPDATE_DREAMER_LOGIN = "UPDATE dreamers SET last_login = ?, last_login_display = ? WHERE email = ?";
    public static final String SQL_UPDATE_DREAMER_PASSWORD = "UPDATE dreamers SET password = ? WHERE email = ?";
    public static final String SQL_UPDATE_DREAMER_PROFILE = "UPDATE dreamers SET first_name = ?, last_name = ? WHERE email = ?";
    public static final String SQL_UPDATE_DREAMER_PROFILE_PIC = "UPDATE images SET profile_image = ? WHERE dreamer_id = ?";
    public static final String SQL_DELETE_DREAMER = "UPDATE dreamers SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, email = ?, password = ?, profile_image_url = ?, role = ?, active = ?, not_locked = ? WHERE email = ?";
    public static final String SQL_SELECT_PROFILE_IMAGE = "SELECT * FROM images";
}
