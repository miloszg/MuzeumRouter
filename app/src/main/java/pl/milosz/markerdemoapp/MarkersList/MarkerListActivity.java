package pl.milosz.markerdemoapp.MarkersList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.milosz.markerdemoapp.R;

public class MarkerListActivity extends AppCompatActivity {
    private static final String TAG = "MarkerListActivity";
    public static ArrayList<Marker> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView markerListView = findViewById(R.id.markerListView);

        MarkerListAdapter adapter = new MarkerListAdapter(this, R.layout.custom_view_layout,list);
        markerListView.setAdapter(adapter);

        markerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"Option Menu Clicked");
                Toast.makeText(MarkerListActivity.this, "chosen option: "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
