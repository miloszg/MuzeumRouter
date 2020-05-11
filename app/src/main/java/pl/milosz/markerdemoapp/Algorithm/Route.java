package pl.milosz.markerdemoapp.Algorithm;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class Route {
    private final ArrayList<City> cities = new ArrayList<>();

    public Route(ArrayList<City> cities) {
        this.cities.addAll(cities);
    }

    public int calculateTotalDistance() {
        int citiesSize = cities.size();
        double totalDistance = cities
                .stream()
                .mapToDouble(x -> {
                    int index = cities.indexOf(x);
                    double returnValue = 0;
                    if (index < citiesSize - 1) {
                        returnValue = x.measureDistance(cities.get(index + 1));
                    }
                    return returnValue;
                })
                .sum() + cities.get(citiesSize - 1).measureDistance(cities.get(0));
        return (int) totalDistance;
    }

    public String toString() {
        return Arrays.toString(cities.toArray());
    }
}
