package pl.milosz.markerdemoapp.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class Route {
    private ArrayList<City> cities = new ArrayList<>();

    public Route(ArrayList<City> cities) {
        this.cities.addAll(cities);
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public int calculateTotalDistance() {
        int citiesSize = this.getCities().size();
        double totalDistance = this.getCities()
                .stream()
                .mapToDouble(x -> {
                    int index = this.getCities().indexOf(x);
                    double returnValue = 0;
                    if (index < citiesSize - 1) {
                        returnValue = x.measureDistance(this.getCities().get(index + 1));
                    }
                    return returnValue;
                })
                .sum() + this.getCities().get(citiesSize - 1).measureDistance(this.getCities().get(0));
        return (int) totalDistance;
    }

    public String toString() {
        return Arrays.toString(cities.toArray());
    }
}
