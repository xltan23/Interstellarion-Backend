package sg.edu.nus.iss.server.models;

public class PlanetSearch {
    
    private String searchName;
    private Double min_mass;
    private Double max_mass;
    private Double min_semi_major_axis;
    private Double max_semi_major_axis;
    private Double max_distance_light_year;

    public String getSearchName() {
        return searchName;
    }
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
    public Double getMin_mass() {
        return min_mass;
    }
    public void setMin_mass(Double min_mass) {
        this.min_mass = min_mass;
    }
    public Double getMax_mass() {
        return max_mass;
    }
    public void setMax_mass(Double max_mass) {
        this.max_mass = max_mass;
    }
    public Double getMin_semi_major_axis() {
        return min_semi_major_axis;
    }
    public void setMin_semi_major_axis(Double min_semi_major_axis) {
        this.min_semi_major_axis = min_semi_major_axis;
    }
    public Double getMax_semi_major_axis() {
        return max_semi_major_axis;
    }
    public void setMax_semi_major_axis(Double max_semi_major_axis) {
        this.max_semi_major_axis = max_semi_major_axis;
    }
    public Double getMax_distance_light_year() {
        return max_distance_light_year;
    }
    public void setMax_distance_light_year(Double max_distance_light_year) {
        this.max_distance_light_year = max_distance_light_year;
    }


    
}
