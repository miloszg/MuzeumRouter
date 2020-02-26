package pl.milosz.markerdemoapp.RouteList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import pl.milosz.markerdemoapp.Map.MapActivity;
import pl.milosz.markerdemoapp.MuseumList.Museum;
import pl.milosz.markerdemoapp.R;

import static pl.milosz.markerdemoapp.Map.MapActivity.mapView;
import static pl.milosz.markerdemoapp.MuseumList.MuseumListActivity.list;

public class RouteListActivity extends AppCompatActivity {
    private static final String TAG = "RouteListActivity";
    public static ArrayList<RouteApp> routeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        aaa();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView routerList = findViewById(R.id.routeListView);

        generateRoute();

        RouteListAdapter adapter = new RouteListAdapter(this, R.layout.contact_view_layout,routeList);
        routerList.setAdapter(adapter);

        routerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"Option Menu Clicked");
                Toast.makeText(RouteListActivity.this, "chosen option: "+position, Toast.LENGTH_SHORT).show();


                switch (position){
                    case 0:
                    case 1:
                        Intent mapIntent = new Intent(getApplicationContext(),MapActivity.class);
                        mapIntent.putExtra("route",position);
                        startActivity(mapIntent);
                        break;
                }
            }
        });
    }

    private void generateRoute() {
        //krolewskie
        ArrayList<Museum> royalMuseums = new ArrayList<>();
        Museum museum1 = new Museum("52.2499786","21.0113804","Rynek Starego Miasta");
        Museum museum2 = new Museum("52.1640891","21.0881525","Pałac w Wilanowie");
        Museum museum3 = new Museum("52.2320719F","21.0259508","Muzeum Wojska Polskiego");
        royalMuseums.add(museum1);
        royalMuseums.add(museum2);
        royalMuseums.add(museum3);
        RouteApp krolewska = new RouteApp("Ścieżka Królewska",royalMuseums,R.drawable.ic_royal);

        //sztuki
        ArrayList<Museum> artMuseums = new ArrayList<>();
        Museum art_museum1 = new Museum("52.2381491","21.0121665","Muzeum Etnograficzne");
        Museum art_museum2 = new Museum("52.1640891","21.0881525","Muzeum Plakatów w Wilanowie");
        Museum art_museum3 = new Museum("52.2327651","21.0019577","Muzeum Sztuki Nowoczesnej");
        artMuseums.add(art_museum1);
        artMuseums.add(art_museum2);
        artMuseums.add(art_museum3);
        RouteApp art = new RouteApp("Ścieżka Sztuki",royalMuseums,R.drawable.ic_flower);

        routeList.add(krolewska);
        routeList.add(art);
    }

    public void aaa() {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream input = this.getResources().openRawResource(R.raw.museums);
            Document doc = builder.parse(input);

            Node osmRoot = doc.getFirstChild();
            NodeList osmXMLNodes = osmRoot.getChildNodes();
            for (int i = 1; i < osmXMLNodes.getLength(); i++) {
                Node item = osmXMLNodes.item(i);
                if (item.getNodeName().equals("node")) {
                    NamedNodeMap attributes = item.getAttributes();
                    NodeList tagXMLNodes = item.getChildNodes();
                    Map<String, String> tags = new HashMap<String, String>();
                    for (int j = 1; j < tagXMLNodes.getLength(); j++) {
                        Node tagItem = tagXMLNodes.item(j);
                        NamedNodeMap tagAttributes = tagItem.getAttributes();
                        if (tagAttributes != null) {
                            tags.put(tagAttributes.getNamedItem("k").getNodeValue(), tagAttributes.getNamedItem("v")
                                    .getNodeValue());
                        }
                    }
                    Node namedItemLat = attributes.getNamedItem("lat");
                    Node namedItemLon = attributes.getNamedItem("lon");

                    String name = tags.get("name");
                    String latitude = namedItemLat.getNodeValue();
                    String longitude = namedItemLon.getNodeValue();

                    Museum museum = new Museum(latitude,longitude,name);
                    list.clear();
                    list.add(museum);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
