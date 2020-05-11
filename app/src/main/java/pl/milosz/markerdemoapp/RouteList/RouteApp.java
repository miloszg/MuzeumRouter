package pl.milosz.markerdemoapp.RouteList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.milosz.markerdemoapp.MarkersList.Marker;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class RouteApp {
    public String title;
    public ArrayList<Marker> routeMarkers;
    public int drawable;
}
