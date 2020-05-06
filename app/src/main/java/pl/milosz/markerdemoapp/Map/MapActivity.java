package pl.milosz.markerdemoapp.Map;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private ImageButton buttonCenter;
    private ImageButton buttonZoom;
    private ImageButton buttonZoomOut;

    private AlertDialog myAlertdialog;
    boolean close = false;
    private double zoom = 14.5;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        setContentView(R.layout.activity_map);
        relativeLayout = findViewById(R.id.mapLayout);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(this));
        Configuration.getInstance().setUserAgentValue(getPackageName());


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
        buttonCenter = findViewById(R.id.ic_map_center);
        buttonZoom = findViewById(R.id.ic_map_add);
        buttonZoomOut = findViewById(R.id.ic_map_minus);

        buttonCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerMap();
            }
        });

        //Starting point
        mapController = mapView.getController();
        mapController.setZoom(zoom);
        GeoPoint startPoint = new GeoPoint(54.4636543, 18.4717798);
        mapController.setCenter(startPoint);

        buttonZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapController.setZoom(++zoom);
            }
        });

        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapController.setZoom(--zoom);
            }
        });

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
        //Bitmap currentIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_arrow_upward_black_24dp);
        //Bitmap currentIcon = ((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.small_arrow, null)).getBitmap();
        locationOverlay.enableMyLocation();
        locationOverlay.disableFollowLocation();
        locationOverlay.setOptionsMenuEnabled(true);
        //locationOverlay.setPersonIcon(currentIcon);
        //locationOverlay.setDirectionArrow(currentIcon,currentIcon);
        centerMap();

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

    private void buildDialog() {
        myAlertdialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View icon_dialog = inflater.inflate(R.layout.dialog_picker, null);
        myAlertdialog.setView(icon_dialog);

        myAlertdialog.show();
        if (close) {
            myAlertdialog.cancel();
            myAlertdialog.dismiss();
        }
    }


    private void centerMap() {
        if (currentLocation != null) {
            GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            mapView.getController().animateTo(myPosition);
        } else {
            Toast.makeText(this, "Proszę włączyć lokalizację", Toast.LENGTH_SHORT).show();
        }
    }

    private void grayedOutMapPicker() {
        ColorMatrix cm = new ColorMatrix();
        float brightness = .5f;
        cm.set(new float[]{
                brightness, 0, 0, 0, 0,    //red
                0, brightness, 0, 0, 0,    //green
                0, 0, brightness, 0, 0,    //blue
                0, 0, 0, 1, 0});    //alpha
        mapView.getOverlayManager().getTilesOverlay().setColorFilter(new ColorMatrixColorFilter(cm));
        buttonAdd.setText("Wybierz punkt na mapie");
        final MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                addMarker(p);
                mapView.getOverlayManager().getTilesOverlay().setColorFilter(null);
                buttonAdd.setText("Dodaj punkt rozpoczęcia wycieczki");
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        mapView.getOverlays().add(new MapEventsOverlay(mReceive));
    }

    public void onClick(View view) {
        String tag = String.valueOf(view.getTag());
        Log.i("clicked", tag);
        Toast.makeText(this, "wybrano " + tag, Toast.LENGTH_SHORT).show();
        close = true;
    }

    private void addMarker(GeoPoint p) {
        org.osmdroid.views.overlay.Marker positionMarker = new org.osmdroid.views.overlay.Marker(mapView);
        GeoPoint position = new GeoPoint(p.getLatitude(), p.getLongitude());
        positionMarker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_location, null));
        positionMarker.setPosition(position);
        positionMarker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(positionMarker);
        close = false;
        buildDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            lm.removeUpdates(this);
        } catch (Exception ex) {
        }

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
        } catch (Exception ex) {
        }

        locationOverlay.enableFollowLocation();
        locationOverlay.enableMyLocation();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lm = null;
        currentLocation = null;
        locationOverlay = null;
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
