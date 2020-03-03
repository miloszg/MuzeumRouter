package pl.milosz.markerdemoapp.RouteList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import pl.milosz.markerdemoapp.R;


public class RouteListAdapter extends ArrayAdapter<RouteApp> {
    private Context routeContext;
    private int routeResource;

    public RouteListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RouteApp> objects) {
        super(context, resource, objects);
        routeContext = context;
        routeResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String header1 = getItem(position).getTitle();
        String header2 = "kliknij, by rozwinąć listę";
        int drawable = getItem(position).getDrawable();

        LayoutInflater inflater = LayoutInflater.from(routeContext);
        convertView = inflater.inflate(routeResource,parent, false);

        TextView header1TextView=convertView.findViewById(R.id.header1);
        TextView header2TextView=convertView.findViewById(R.id.header2);
        ImageView imageView=convertView.findViewById(R.id.imageView);
        header1TextView.setText(header1);
        header2TextView.setText(header2);
        imageView.setImageResource(drawable);

        return convertView;
    }
}
