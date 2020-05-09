package pl.milosz.markerdemoapp.Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BruteForce {
    static int permutation = 1;
    private final ArrayList<Route> shortestRoutes = new ArrayList<>();

    public Route findShortestRoute(ArrayList<City> cities) {
        return permuteCities(0, new Route(cities), new Route(cities))
                .stream()
                .sorted(Comparator.comparingInt(Route::calculateTotalDistance))
                .collect(Collectors.toList()).get(0);
    }

    public ArrayList<Route> permuteCities(int x, Route currentRoute, Route shortestRoute) {
        currentRoute.getCities().stream().filter(y -> currentRoute.getCities().indexOf(y) >= x).forEach(y -> {
            int indexOfY = currentRoute.getCities().indexOf(y);
            Collections.swap(currentRoute.getCities(), indexOfY, x);
            permuteCities(x + 1, currentRoute, shortestRoute);
            Collections.swap(currentRoute.getCities(), x, indexOfY);
        });

        if (x == currentRoute.getCities().size() - 1) {
            System.out.println(currentRoute + " |      " + getTotalDistance(currentRoute));
            if (currentRoute.calculateTotalDistance() <= shortestRoute.calculateTotalDistance()) {
                shortestRoute.getCities().clear();
                shortestRoute.getCities().addAll(currentRoute.getCities());
                addToShortestRoutes(new Route(shortestRoute.getCities()));
            }
        }
        return shortestRoutes;
    }

    public void addToShortestRoutes(Route route) {
        shortestRoutes.removeIf(x -> x.calculateTotalDistance() > route.calculateTotalDistance());
        shortestRoutes.add(route);
    }

    private String getTotalDistance(Route currentRoute) {
        return Integer.toString(currentRoute.calculateTotalDistance());
    }
}
