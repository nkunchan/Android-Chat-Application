package com.example.homework7;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nikita on 11/19/2016.
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {
    static Context context;
    ArrayList<Inbox_msg> inbox_msg_list;
    onRowItemClick listener;

    public InboxAdapter(Context context, ArrayList<Inbox_msg> inbox_msg_list) {
        this.context = context;
        this.inbox_msg_list = inbox_msg_list;
    }

    public InboxAdapter(Context context, ArrayList<Inbox_msg> nlist, onRowItemClick listener) {
        this.context = context;
        this.inbox_msg_list = nlist;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View fView = inflater.inflate(R.layout.item_row_lyt_inbox_msg, parent, false);
        ViewHolder holder = new ViewHolder(fView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Inbox_msg newsItem = inbox_msg_list.get(position);

//        holder.title.setTextColor(Color.parseColor("#00aaff"));
//        holder.imgViewIcon.setBackgroundResource(R.drawable.ic_circle);
        holder.bind(newsItem, listener);
    }

    @Override
    public int getItemCount() {
        return inbox_msg_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView dp;
        TextView dis_name,msgtext,time;
        RelativeLayout list_row;



        public ViewHolder(View itemView) {
            super(itemView);
           dp= (ImageView) itemView.findViewById(R.id.dis_pic);
            dis_name= (TextView) itemView.findViewById(R.id.dp_name);
            msgtext= (TextView) itemView.findViewById(R.id.rcvd_msg);
            time=(TextView) itemView.findViewById(R.id.rcvd_time);
            list_row= (RelativeLayout) itemView.findViewById(R.id.rcvd_id);


        }

        public void bind(final Inbox_msg msg, final onRowItemClick listener) {
            if(msg.getDp()!=null)
                Picasso.with(context).load(msg.getDp()).into(dp);
            Log.d("msg in inbox",msg.toString());

            if(msg.getStatus().compareTo("Unread")==0)
            {
               // list_row.setBackgroundColor(Color.parseColor("#00ff00"));
                msgtext.setTextColor(Color.parseColor("#00ff00"));
                time.setTextColor(Color.parseColor("#00ff00"));
            }
            dis_name.setText(msg.getDis_name());
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            SimpleDateFormat fm2=new SimpleDateFormat("h:mm a, MM-dd-yy");

                Date d = null;
                try {
                    d = df.parse(msg.getTime());
                    //PrettyTime pt = new PrettyTime(d);
                    //vh2.rcv_msg_time.setText("Received at " + fm2.format(d));
                    time.setText("Received at " + fm2.format(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            if(msg.getContent().compareTo("msg")==0)
            {
                msgtext.setText(msg.getMessage());
            }

            else if(msg.getContent().compareTo("img")==0)
            {
                msgtext.setText("You received an image");
            }


//            uploadedimg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClick(newItem);
//                }
//            });
//
//            imgdel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onDeleteClick(newItem);
//                }
//            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(msg,getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(msg,getAdapterPosition());
                    return false;
                }
            });





        }



    }


    public interface onRowItemClick {
//        void onClick(Inbox_msg img);
//        void onDeleteClick(Inbox_msg img);
void onItemClick(Inbox_msg item, int position);
        void onLongClick(Inbox_msg item, int position);
    }


}
