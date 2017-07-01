package com.example.homework7;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by priya on 11/18/2016.
 */

public class ImageAdapterForOwn extends RecyclerView.Adapter<ImageAdapterForOwn.ViewHolder> {
    static Context context;
    ArrayList<UserImages> ownImgList;
    onRowItemClick listener;

    public ImageAdapterForOwn(Context context, ArrayList<UserImages> ownImgList) {
        this.context = context;
        this.ownImgList = ownImgList;
    }

    public ImageAdapterForOwn(Context context, ArrayList<UserImages> nlist, onRowItemClick listener) {
        this.context = context;
        this.ownImgList = nlist;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View fView = inflater.inflate(R.layout.item_row_lyt_for_user, parent, false);
        ViewHolder holder = new ViewHolder(fView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserImages newsItem = ownImgList.get(position);
        holder.bind(newsItem, listener);
    }

    @Override
    public int getItemCount() {
        return ownImgList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView uploadedimg,imgdel;



        public ViewHolder(View itemView) {
            super(itemView);
            uploadedimg=(ImageView) itemView.findViewById(R.id.imgOfUser);
            imgdel=(ImageView)itemView.findViewById(R.id.imgdel);


        }

        public void bind(final UserImages newItem, final onRowItemClick listener) {
            Picasso.with(context).load(newItem.getImage()).into(uploadedimg);
            uploadedimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(newItem);
                }
            });

            imgdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(newItem);
                }
            });
        }



    }

    public interface onRowItemClick {
        void onClick(UserImages img);
        void onDeleteClick(UserImages img);
    }


}
