package pl.milosz.markerdemoapp.MuseumList;

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


public class MuseumListAdapter extends ArrayAdapter<Museum> {
    private Context contactContext;
    private int contactResource;

    public MuseumListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Museum> objects) {
        super(context, resource, objects);
        contactContext = context;
        contactResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String header1 = getItem(position).getTitle();
        String header2 = getItem(position).getLat() + " " + getItem(position).getLon();
        int drawable = R.drawable.ic_flower;

        LayoutInflater inflater = LayoutInflater.from(contactContext);
        convertView = inflater.inflate(contactResource,parent, false);

        TextView header1TextView=convertView.findViewById(R.id.header1);
        TextView header2TextView=convertView.findViewById(R.id.header2);
        ImageView imageView=convertView.findViewById(R.id.contactImageView);
        header1TextView.setText(header1);
        header2TextView.setText(header2);
        imageView.setImageResource(drawable);

        return convertView;
    }
}
