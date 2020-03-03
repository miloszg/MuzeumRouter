package pl.milosz.markerdemoapp.Map;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import pl.milosz.markerdemoapp.MarkersList.Marker;
import pl.milosz.markerdemoapp.R;

import static pl.milosz.markerdemoapp.MarkersList.MarkerListActivity.list;

public class MapActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "MapActivity";
    IMapController mapController;
    public static MapView mapView;
    private LocationManager lm;
    private Location currentLocation = null;
    private MyLocationNewOverlay locationOverlay;
    private Button buttonAdd;
    private TextView chooseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        setContentView(R.layout.activity_map);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(this));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        chooseText= findViewById(R.id.chooseTextView);
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.onAttachedToWindow();
        buttonAdd = findViewById(R.id.addLocationButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grayedOutMapPicker();
            }
        });

        //Starting point
        mapController = mapView.getController();
        mapController.setZoom(14.2);
        GeoPoint startPoint = new GeoPoint(54.4636543, 18.4717798);
        mapController.setCenter(startPoint);

        //Compass
        CompassOverlay mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mapView);
        mCompassOverlay.enableCompass();

        //ScaleBar
        final DisplayMetrics dm = this.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        //Current location marker
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        Bitmap currentIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_navigation);
        //Bitmap currentIcon = ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ic_navigation, null)).getBitmap();
        locationOverlay.enableMyLocation();
        locationOverlay.enableFollowLocation();
        locationOverlay.setOptionsMenuEnabled(true);
        //locationOverlay.setDirectionArrow(currentIcon,currentIcon);

        if (currentLocation != null) {
            GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            mapView.getController().animateTo(myPosition);
        }

        //Adding overlays to mapView
        for (Marker m : list) {
            org.osmdroid.views.overlay.Marker marker = new org.osmdroid.views.overlay.Marker(mapView);
            GeoPoint position = new GeoPoint(Float.valueOf(m.getLat()), Float.valueOf(m.getLon()));

            marker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_flower, null));
            marker.setPosition(position);
            marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);
        }
        mapView.getOverlays().add(locationOverlay);
        mapView.getOverlays().add(mCompassOverlay);
        mapView.getOverlays().add(scaleBarOverlay);


    }

    private void grayedOutMapPicker(){
        ColorMatrix cm = new ColorMatrix();
        float brightness =.5f;
        cm.set(new float[] {
                brightness, 0, 0, 0, 0,    //red
                0, brightness, 0, 0, 0,    //green
                0, 0, brightness, 0, 0,    //blue
                0, 0, 0, 1, 0});    //alpha
        mapView.getOverlayManager().getTilesOverlay().setColorFilter(new ColorMatrixColorFilter(cm));
        chooseText.setVisibility(View.VISIBLE);
        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(getBaseContext(),"Wybrano:" +p.getLatitude() + " - "+p.getLongitude(), Toast.LENGTH_LONG).show();
                chooseText.setVisibility(View.INVISIBLE);
                mapView.getOverlayManager().getTilesOverlay().setColorFilter(null);
                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        mapView.getOverlays().add(new MapEventsOverlay(mReceive));
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
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
