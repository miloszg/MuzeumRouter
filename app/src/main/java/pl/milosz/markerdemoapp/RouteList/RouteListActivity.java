package pl.milosz.markerdemoapp.RouteList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import pl.milosz.markerdemoapp.MarkersList.Marker;
import pl.milosz.markerdemoapp.R;

public class RouteListActivity extends AppCompatActivity {
    private static final String TAG = "RouteListActivity";
    public static ArrayList<RouteApp> routeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView routerList = findViewById(R.id.routeListView);


        RouteListAdapter adapter = new RouteListAdapter(this, R.layout.custom_view_layout,routeList);
        routerList.setAdapter(adapter);
        ArrayList<Marker> routeArray = new ArrayList<>();
        RouteApp route = new RouteApp("Ścieżka Leśna",routeArray,R.drawable.ic_flower);
        routeList.add(route);

        int doubleTest = 1;

        routerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"Option Menu Clicked");
                Toast.makeText(RouteListActivity.this, "chosen option: "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
