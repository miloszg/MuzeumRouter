package pl.milosz.markerdemoapp.Algorithm;

public class City {
    private static final double EARTH_RADIUS = 6378.137;
    private static final double DEG_TO_RAD = Math.PI / 180;

    private double longitude;
    private double latitude;

    private String name;

    public double measureDistance(City city) {
        double deltaLongitude = city.getLongitude() - this.getLongitude();
        double deltaLatitude = city.getLatitude() - this.getLatitude();
        double a = Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(this.getLatitude())
                * Math.cos(city.getLatitude())
                * Math.pow(Math.sin(deltaLongitude / 2), 2);
        return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.longitude = longitude * DEG_TO_RAD;
        this.latitude = latitude * DEG_TO_RAD;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
