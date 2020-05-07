package pl.milosz.markerdemoapp.Algorithm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Toast;

import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import androidx.core.content.res.ResourcesCompat;
import pl.milosz.markerdemoapp.R;

public class RouteFinder extends AsyncTask<ArrayList<GeoPoint>, Void, Road> {

  private final RoadManager roadManager;
  private final MapView mapView;
  private final Context context;

  public RouteFinder(MapView mapView, Context context) {
    this.context = context;
    this.roadManager = new MapQuestRoadManager("Gnw2RCzFV27bTy3ui3SXGBva7rmc2X7L");
    this.roadManager.addRequestOption("routeType=bicycle");
    this.mapView = mapView;
  }

  private ArrayList<Marker> markers = new ArrayList<>();

  @Override
  protected Road doInBackground(ArrayList<GeoPoint>... waypoints) {
    Road road = roadManager.getRoad(waypoints[0]);

    Drawable nodeIcon = ResourcesCompat.getDrawable(context.getResources(), R.drawable.marker_node, null);
    for (int i = 0; i < road.mNodes.size(); i++) {
      RoadNode node = road.mNodes.get(i);
      Marker nodeMarker = new Marker(mapView);
      nodeMarker.setPosition(node.mLocation);
      nodeMarker.setIcon(nodeIcon);
      nodeMarker.setTitle("Step " + i);
      nodeMarker.setSnippet(node.mInstructions);
      nodeMarker.setSubDescription(Road.getLengthDurationText(context, node.mLength, node.mDuration));
//      Drawable icon = ResourcesCompat.getDrawable(context.getResources(), R.drawable.marker_node, null);
//      nodeMarker.setImage(icon);
      markers.add(nodeMarker);
    }

    return road;
  }

  @Override
  protected void onPostExecute(Road road) {
    showSummary(Road.getLengthDurationText(context, road.mLength, road.mDuration));
    Polyline polyline = RoadManager.buildRoadOverlay(road);
    mapView.getOverlays().addAll(markers);
    mapView.getOverlays().add(polyline);
    mapView.invalidate();
  }


  private void showSummary(String lengthDurationText) {
    Toast.makeText(context,
        "Podsumowanie: " + lengthDurationText, Toast.LENGTH_LONG).show();
  }
}
