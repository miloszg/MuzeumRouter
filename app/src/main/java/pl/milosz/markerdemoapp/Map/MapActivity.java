package pl.milosz.markerdemoapp.Map;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import pl.milosz.markerdemoapp.MuseumList.Museum;
import pl.milosz.markerdemoapp.R;
import static pl.milosz.markerdemoapp.MuseumList.MuseumListActivity.list;

public class MapActivity extends AppCompatActivity {
    private static final String TAG = "MapActivity";

    public static MapView mapView;

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


        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.onAttachedToWindow();
        IMapController mapController = mapView.getController();
        mapController.setZoom(16.2);

        GeoPoint startPoint = new GeoPoint(52.2305, 21.00388);
        mapController.setCenter(startPoint);

        for(Museum m : list){
            Marker marker = new Marker(mapView);
            GeoPoint position = new GeoPoint(Float.valueOf(m.getLat()), Float.valueOf(m.getLon()));

            marker.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_museum,null));
            marker.setPosition(position);
            marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
            marker.setDraggable(true);
            marker.setTitle(m.getTitle());
            marker.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble,mapView));
            mapView.getOverlays().add(marker);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
