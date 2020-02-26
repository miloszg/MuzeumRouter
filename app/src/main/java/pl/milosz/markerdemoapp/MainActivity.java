package pl.milosz.markerdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import pl.milosz.markerdemoapp.MuseumList.MuseumListActivity;
import pl.milosz.markerdemoapp.Map.MapActivity;
import pl.milosz.markerdemoapp.RouteList.RouteListActivity;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        CardView cardView = findViewById(R.id.cardMap);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent=new Intent(getApplicationContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });

        CardView markerView = findViewById(R.id.cardMarkers);
        markerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent markerIntent=new Intent(getApplicationContext(), MuseumListActivity.class);
                startActivity(markerIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_map:
                Intent mapIntent = new Intent(this, MapActivity.class);
                startActivity(mapIntent);
                break;
            case R.id.nav_marker:
                Intent markerIntent = new Intent(this, MuseumListActivity.class);
                startActivity(markerIntent);
                break;
            case R.id.nav_route:
                Intent routeIntent = new Intent(this, RouteListActivity.class);
                startActivity(routeIntent);
                break;
            case R.id.nav_settings:
                Intent intent_settings = new Intent();
                intent_settings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent_settings.setData(uri);
                startActivity(intent_settings);
                break;
            case R.id.nav_help:
                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
