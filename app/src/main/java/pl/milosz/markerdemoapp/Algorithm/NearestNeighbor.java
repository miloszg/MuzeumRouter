package pl.milosz.markerdemoapp.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class NearestNeighbor {

    public Route findShortestRoute(ArrayList<City> cities) {
        ArrayList<City> shortestRoute = new ArrayList<>(cities.size());
        City city = cities.get(0);
        updateRoutes(shortestRoute, cities, city);
        while (cities.size() >= 1) {
            city = getNextCity(cities, city);
            updateRoutes(shortestRoute, cities, city);
        }

        return new Route(shortestRoute);
    }

    public Route findShortestRouteWithLimitDistance(ArrayList<City> cities, int limitDistance) {
        ArrayList<City> shortestRoute = new ArrayList<>(cities.size());
        City startCity = cities.get(0);
        City city = startCity;
        updateRoutes(shortestRoute, cities, city);
        while (cities.size() >= 1) {
            city = getNextCity(cities, city);

            if (isFurtherThanLimit(city, shortestRoute, limitDistance, startCity)) break;
            else updateRoutes(shortestRoute, cities, city);
        }

        return new Route(shortestRoute);
    }

    private boolean isFurtherThanLimit(City city, ArrayList<City> shortestRoute, int limitDistance, City finalCity) {
        ArrayList<City> shortestRouteToBe = new ArrayList<>(shortestRoute);
        shortestRouteToBe.add(city);
        shortestRouteToBe.add(finalCity);
        double ROUTE_ERROR = 0.8;
        return new Route(shortestRouteToBe).calculateTotalDistance() > ROUTE_ERROR * limitDistance;
    }

    private void updateRoutes(ArrayList<City> shortestRoute, ArrayList<City> cities, City city) {
        shortestRoute.add(city);
        cities.remove(city);
    }

    private City getNextCity(ArrayList<City> cities, City city) {
        return cities.stream().min((city1, city2) -> {
            int flag = 0;
            double city1Dist = city1.measureDistance(city);
            double city2Dist = city2.measureDistance(city);
            if (city1Dist < city2Dist) {
                flag = -1;
            } else if (city1Dist > city2Dist) {
                flag = 1;
            }
            return flag;
        }).get();
    }
}
