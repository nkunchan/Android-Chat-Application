package com.example.homework7;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Nikita on 11/18/2016.
 */
public class OtherUsersAdapter extends ArrayAdapter<OtherUsers> {

    ArrayList<OtherUsers> objects;
    Context context;
    int resource;


    public OtherUsersAdapter(Context context, int resource, ArrayList<OtherUsers> objects) {
        super(context, resource, objects);
        this.objects= objects;
        this.context=context;
        this.resource=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, null);

       TextView dpname = (TextView) view.findViewById(R.id.dp_name);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        dpname.setText(objects.get(position).getDisplayName().toString());
//        byte [] encodeByte=Base64.decode(objects.get(position).getDp(), Base64.DEFAULT);
//        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
      //  image.setImageBitmap(objects.get(position).getDp());
        if(objects.get(position).getDp()!=null) {
            Picasso.with(context).load(objects.get(position).getDp()).into(image);
        }


        return view;
        //return super.getView(position, convertView, parent);
    }
}
