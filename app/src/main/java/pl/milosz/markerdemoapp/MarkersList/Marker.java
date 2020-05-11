package pl.milosz.markerdemoapp.MarkersList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Marker {
    public String lat;
    public String lon;
    public String title;
}
