package pl.milosz.markerdemoapp.Map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import pl.milosz.markerdemoapp.MuseumList.Museum;
import pl.milosz.markerdemoapp.R;

import static pl.milosz.markerdemoapp.MuseumList.MuseumListActivity.list;

public class MapActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "MapActivity";
    IMapController mapController;
    public static MapView mapView;
    private LocationManager lm;
    private Location currentLocation = null;
    private MyLocationNewOverlay locationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        setContentView(R.layout.activity_map);

        Intent receivedIntent = getIntent();
        int route = receivedIntent.getIntExtra("route", -1);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(this));
        Configuration.getInstance().setUserAgentValue(getPackageName());


        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setMultiTouchControls(true);
        mapView.onAttachedToWindow();
        mapController = mapView.getController();
        mapController.setZoom(16.2);

        GeoPoint startPoint = new GeoPoint(52.2305, 21.00388);
        mapController.setCenter(startPoint);

        CompassOverlay mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(mCompassOverlay);

        final DisplayMetrics dm = this.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),
                mapView);
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        locationOverlay.setOptionsMenuEnabled(true);
        ;

        mapView.getOverlays().add(locationOverlay);
        if (currentLocation != null) {
            GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            mapView.getController().animateTo(myPosition);
        }

        for (Museum m : list) {
            Marker marker = new Marker(mapView);
            GeoPoint position = new GeoPoint(Float.valueOf(m.getLat()), Float.valueOf(m.getLon()));

            marker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_museum, null));
            marker.setPosition(position);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setDraggable(true);
            marker.setTitle(m.getTitle());
            marker.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapView));
            mapView.getOverlays().add(marker);
        }

        switch (route) {
            case 0:
                generateRoyalRoute();
                break;
            case 1:
                generateArtRoute();
                break;
        }

    }

    private void generateRoyalRoute() {
        RoadManager roadManager = new OSRMRoadManager(this);

        GeoPoint start = new GeoPoint(52.2499786F, 21.0113804F);
        GeoPoint mid = new GeoPoint(52.1640891F, 21.0881525F);
        GeoPoint end = new GeoPoint(52.2320719F, 21.0259508F);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(start);
        waypoints.add(mid);
        waypoints.add(end);

        Road road = roadManager.getRoad(waypoints);
        Polyline roadLine = RoadManager.buildRoadOverlay(road);

        roadLine.setColor(Color.RED);
        roadLine.setPoints(waypoints);
        roadLine.setGeodesic(true);
        mapController.setCenter(start);
        mapView.getOverlayManager().add(roadLine);
    }

    private void generateArtRoute() {
        GeoPoint start = new GeoPoint(52.2381491F, 21.0121665F);
        GeoPoint mid = new GeoPoint(52.1640891F, 21.0881525F);
        GeoPoint end = new GeoPoint(52.2327651F, 21.0019577F);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(start);
        waypoints.add(mid);
        waypoints.add(end);

        Polyline line = new Polyline(mapView);
        line.setColor(Color.GREEN);
        line.setPoints(waypoints);
        line.setGeodesic(true);
        mapController.setCenter(start);
        mapView.getOverlayManager().add(line);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            lm.removeUpdates(this);
        }catch (Exception ex){}

        locationOverlay.disableFollowLocation();
        locationOverlay.disableMyLocation();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        } catch (Exception ex) {
        }

        try {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0l, 0f, this);
        }catch (Exception ex){}

        locationOverlay.enableFollowLocation();
        locationOverlay.enableMyLocation();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lm=null;
        currentLocation=null;
        locationOverlay=null;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
