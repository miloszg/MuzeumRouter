package pl.milosz.markerdemoapp.RouteList;

import java.util.ArrayList;

import pl.milosz.markerdemoapp.MarkersList.Marker;

public class RouteApp {
    public String title;
    public ArrayList<Marker> routeMarkers;
    public int drawable;

    public RouteApp(String title, ArrayList<Marker> routeMarkers, int drawable) {
        this.title = title;
        this.routeMarkers = routeMarkers;
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Marker> getRouteMarkers() {
        return routeMarkers;
    }

    public void setRouteMarkers(ArrayList<Marker> routeMarkers) {
        this.routeMarkers = routeMarkers;
    }
}
