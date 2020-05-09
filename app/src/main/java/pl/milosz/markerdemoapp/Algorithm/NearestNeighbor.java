package pl.milosz.markerdemoapp.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class NearestNeighbor {
    public Route findShortestRoute(ArrayList<City> cities) {
        ArrayList<City> shortesRoute = new ArrayList<>(cities.size());
        System.out.println("Initial Route          ==> " + Arrays.toString(cities.toArray()));
        System.out.println("total distance:  " + new Route(cities).calculateTotalDistance());
        System.out.println("------------");
        City city = cities.get(0);
        updateRoutes(shortesRoute, cities, city);
        while (cities.size() >= 1) {
            city = getNextCity(cities, city);
            updateRoutes(shortesRoute, cities, city);
        }

        return new Route(shortesRoute);
    }

    private void updateRoutes(ArrayList<City> shortestRoute, ArrayList<City> cities, City city) {
        shortestRoute.add(city);
        cities.remove(city);

        System.out.println("Cities in Shortest Route  => " + Arrays.toString(shortestRoute.toArray()));
        System.out.println("Remaining cities          => " + Arrays.toString(cities.toArray()));
        System.out.println("");
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
