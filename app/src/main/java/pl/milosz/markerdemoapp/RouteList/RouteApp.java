package pl.milosz.markerdemoapp.RouteList;

import java.util.ArrayList;

import pl.milosz.markerdemoapp.MuseumList.Museum;

public class RouteApp {
    public String title;
    public ArrayList<Museum> routeMuseums;
    public int drawable;

    public RouteApp(String title, ArrayList<Museum> routeMuseums, int drawable) {
        this.title = title;
        this.routeMuseums = routeMuseums;
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

    public ArrayList<Museum> getRouteMuseums() {
        return routeMuseums;
    }

    public void setRouteMuseums(ArrayList<Museum> routeMuseums) {
        this.routeMuseums = routeMuseums;
    }
}
