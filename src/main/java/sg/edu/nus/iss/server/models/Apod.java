package sg.edu.nus.iss.server.models;

import jakarta.json.JsonObject;

public class Apod {
    
    private String date;
    private String explanation;
    private String hdurl;
    private String title;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getExplanation() {
        return explanation;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    public String getHdurl() {
        return hdurl;
    }
    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public static Apod create(JsonObject jsonApod) {
        Apod apod = new Apod();
        apod.setDate(jsonApod.getString("date"));
        apod.setExplanation(jsonApod.getString("explanation"));
        apod.setHdurl(jsonApod.getString("hdurl"));
        apod.setTitle(jsonApod.getString("title"));
        return apod;
    }
}
