package pl.milosz.markerdemoapp.MuseumList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

import pl.milosz.markerdemoapp.R;

public class MuseumListActivity extends AppCompatActivity {
    private static final String TAG = "MuseumListActivity";
    public static ArrayList<Museum> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        aaa();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView contactList = findViewById(R.id.contactListView);

        MuseumListAdapter adapter = new MuseumListAdapter(this, R.layout.contact_view_layout,list);
        contactList.setAdapter(adapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"Option Menu Clicked");
                Toast.makeText(MuseumListActivity.this, "chosen option: "+position, Toast.LENGTH_SHORT).show();
            }
        });
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
                    list.add(museum);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
