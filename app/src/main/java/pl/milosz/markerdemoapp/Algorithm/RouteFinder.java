package pl.milosz.markerdemoapp.Algorithm;

/*import org.jacop.constraints.XneqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;*/

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
//import com.graphhopper.GHRequest;
//import com.graphhopper.GHResponse;
//import com.graphhopper.GraphHopper;
//import com.graphhopper.PathWrapper;
//import com.graphhopper.util.Parameters;
//import com.graphhopper.util.StopWatch;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class RouteFinder {

//    private GraphHopper hopper;
    private File mapsFolder;
    private String currentArea = "berlin";

    public RouteFinder() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            log("GraphHopper is not usable without an external storage!");
            return;
        }
        mapsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "/graphhopper/maps/");

//        GraphHopper tmpHopp = new GraphHopper().forMobile();
//        tmpHopp.load(new File(mapsFolder, currentArea).getAbsolutePath() + "-gh");
//        log("found graph " + tmpHopp.getGraphHopperStorage().toString() + ", nodes:" + tmpHopp.getGraphHopperStorage().getNodes());
//        hopper = tmpHopp;
    }

    public String find() {
        final double fromLat = 0.0d;
        final double fromLon = 0.0d;
        final double toLat = 1.0d;
        final double toLon = 1.0d;

        log("calculating path ...");

        new AsyncTask<Void, Void, PathWrapper>() {
            float time;

            protected PathWrapper doInBackground(Void... v) {
                StopWatch sw = new StopWatch().start();
                GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon).
                        setAlgorithm(Parameters.Algorithms.DIJKSTRA_BI);
                req.getHints().
                        putObject(Parameters.Routing.INSTRUCTIONS, "false");
                GHResponse resp = hopper.route(req);
                time = sw.stop().getSeconds();
                return resp.getBest();
            }

            protected void onPostExecute(PathWrapper resp) {
                if (!resp.hasErrors()) {
                    log("from:" + fromLat + "," + fromLon + " to:" + toLat + ","
                            + toLon + " found path with distance:" + resp.getDistance()
                            / 1000f + ", nodes:" + resp.getPoints().getSize() + ", time:"
                            + time + " " + resp.getDebugInfo());
                    log("the route is " + (int) (resp.getDistance() / 100) / 10f
                            + "km long, time:" + resp.getTime() / 60000f + "min, debug:" + time);

                } else {
                    log("Error:" + resp.getErrors());
                }
            }
        }.execute();

        /*Store store = new Store();

        int size = 4;
        // define finite domain variables
        IntVar[] v = new IntVar[size];
        for (int i = 0; i < size; i++) {
            v[i] = new IntVar(store, "v" + i, 1, size);
        }
        // define constraints
        store.impose(new XneqY(v[0], v[1]));
        store.impose(new XneqY(v[0], v[2]));
        store.impose(new XneqY(v[1], v[2]));
        store.impose(new XneqY(v[1], v[3]));
        store.impose(new XneqY(v[2], v[3]));

        // search for a solution and print results
        Search<IntVar> search = new DepthFirstSearch<>();
        SelectChoicePoint<IntVar> select =
                new InputOrderSelect<>(store, v, new IndomainMin<>());
        boolean result = search.labeling(store, select);

        if (result) {
            return "Solution: " + v[0] + ", " + v[1] + ", " + v[2] + ", " + v[3];
        } else {
            return "*** No";
        }*/
        return "";
    }

    private void log(String msg) {
        Log.d("RouteFinder", msg);
    }
}
