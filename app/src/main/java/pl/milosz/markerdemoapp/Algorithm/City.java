package pl.milosz.markerdemoapp.Algorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class City {
    private static final double EARTH_RADIUS = 6378.137;
    private static final double DEG_TO_RAD = Math.PI / 180;

    private double latitude;
    private double longitude;

    public double measureDistance(City city) {
        double deltaLongitude = city.getLongitude() - longitude;
        double deltaLatitude = city.getLatitude() - latitude;
        double a = Math.pow(Math.sin(deltaLatitude * DEG_TO_RAD / 2), 2)
                + Math.cos(latitude * DEG_TO_RAD)
                * Math.cos(city.getLatitude() * DEG_TO_RAD)
                * Math.pow(Math.sin(deltaLongitude * DEG_TO_RAD / 2), 2);
        return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
