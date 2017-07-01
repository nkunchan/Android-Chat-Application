package com.example.homework7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GVAdapter extends BaseAdapter {
    Context context;
    ArrayList<UserImages> glist;
    int res;
    public GVAdapter(Context context,ArrayList<UserImages> glist, int resource){
        this.context=context;
        this.glist=glist;
        res=resource;
    }
    @Override
    public int getCount() {
        return glist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return glist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(res,parent,false);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.txtDateOfUpload);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView2);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        TextView name = holder.date;
        ImageView image = holder.image;
        UserImages photo =glist.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
        String date1 = photo.getDateofupload();
        try {
            Date d1 = sdf.parse(date1);
            PrettyTime pt = new PrettyTime(d1);
            name.setText("Uploaded " + pt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.with(context).load(photo.image).into(image);
        return convertView;
    }

    static class ViewHolder {
        TextView date;
        ImageView image;
    }
}

