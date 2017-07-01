package com.example.homework7;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nikita on 11/19/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static Context context;
    ArrayList<Message> msglist;
    onRowItemClick listener;
    private final int SENT = 0, RECEIVED = 1;



    public ChatAdapter(Context context, ArrayList<Message> nlist,onRowItemClick listener) {
        this.context = context;
        msglist = nlist;
        this.listener=listener;
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        if (msglist.get(position).getType().compareTo("sent")==0) {
            return SENT;
        } else if (msglist.get(position).getType().compareTo("received")==0) {
            return RECEIVED;
        }
        return -1;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // Context context = parent.getContext();
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case SENT:
                View fView = inflater.inflate(R.layout.msg_content_sent, parent, false);
                //fView=parent.findViewById(R.id.sent_img_time);

                holder = new ViewHolder1(fView);

                break;
            case RECEIVED:
                View fView1 = inflater.inflate(R.layout.msg_content_layout, parent, false);
                holder = new ViewHolder2(fView1);

                break;
            default:
                View fView3 = inflater.inflate(R.layout.msg_content_layout, parent, false);
                holder = new ViewHolder1(fView3);

                break;

        }
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      //  Message msg = msglist.get(position);
        //holder.bind(msg,listener);
        switch (holder.getItemViewType()) {
            case SENT:
                ViewHolder1 vh1 = (ViewHolder1) holder;
                configureViewHolder1(vh1, position);
                break;
            case RECEIVED:
                ViewHolder2 vh2 = (ViewHolder2) holder;
                configureViewHolder2(vh2, position);
                break;
            default:
             //   ViewHolder1 vh3 = (ViewHolder1) holder;
              //  configureViewHolder1(vh3, position);
                break;
        }

    }


    private void configureViewHolder1(ViewHolder1 vh1, final int position) {
      final Message msg = (Message) msglist.get(position);
        if(msg!=null)
        {
            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            SimpleDateFormat fm2=new SimpleDateFormat("h:mm a, MM-dd-yy");
            if(msg.getContent().compareTo("msg")==0) {
                vh1.sent_msg_time.setVisibility(View.VISIBLE);
                vh1.sentmsg.setVisibility(View.VISIBLE);
                vh1.sentimg.setVisibility(View.GONE);
                vh1.sent_img_time.setVisibility(View.GONE);
                vh1.sentmsg.setText(msg.getMessage());
                vh1.sent_msg_time.setTextColor(Color.parseColor("#000000"));
                vh1.sentmsg.setTextColor(Color.parseColor("#000000"));
                Date d = null;
                try {
                    d = df.parse(msg.getTime());
                    // PrettyTime pt = new PrettyTime(d);
                    vh1.sent_msg_time.setText("Sent at " + fm2.format(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
            else if(msg.getContent().compareTo("img")==0) {
                vh1.sentimg.setVisibility(View.VISIBLE);
                vh1.sent_img_time.setVisibility(View.VISIBLE);
                Date d = null;
                try {
                    d = df.parse(msg.getTime());
                    // PrettyTime pt = new PrettyTime(d);
                    vh1.sent_img_time.setText("Sent at " + fm2.format(d));
                    vh1.sentmsg.setVisibility(View.GONE);
                    vh1.sent_msg_time.setVisibility(View.GONE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Picasso.with(context).load(msg.getMessage()).into(vh1.sentimg);


                //vh2.list_row.setBackgroundColor(Color.parseColor("#73ba4e"));
                vh1.sent_img_time.setTextColor(Color.parseColor("#000000"));


//                    vh1.title.setTextColor(Color.parseColor("#00aaff"));
//                    v

                //vh2.list_row.setBackgroundColor(Color.TRANSPARENT);




            }
            vh1.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //listener.longItemClick(msg,position);
                    listener.longItemClick(msg,position);
                    return false;
                }

            });





        }


}

    private void configureViewHolder2(ViewHolder2 vh2, final int position) {
        // vh2.getImageView().setImageResource(R.drawable.sample_golden_gate);
        final Message msg = (Message) msglist.get(position);
        if(msg!=null)
        {
            Log.d("msg",msg.toString());

            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            SimpleDateFormat fm2=new SimpleDateFormat("h:mm a, MM-dd-yy");
            if(msg.getContent().compareTo("msg")==0) {
                vh2.rcvdmsg.setVisibility(View.VISIBLE);
                vh2.rcv_msg_time.setVisibility(View.VISIBLE);
                vh2.rcvdimg.setVisibility(View.GONE);
                vh2.rcvd_img_time.setVisibility(View.GONE);
                Date d = null;
                try {
                    d = df.parse(msg.getTime());
                    //PrettyTime pt = new PrettyTime(d);
                    vh2.rcv_msg_time.setText("Received at " + fm2.format(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                vh2.rcvdmsg.setText(msg.getMessage());
                if(msg.getStatus().compareTo("Unread")==0)
                {

                    //vh2.list_row.setBackgroundColor(Color.parseColor("#73ba4e"));
                    vh2.rcv_msg_time.setTextColor(Color.parseColor("#73ba4e"));
                    vh2.rcvdmsg.setTextColor(Color.parseColor("#73ba4e"));


//                    vh1.title.setTextColor(Color.parseColor("#00aaff"));
//                    v
                }
                else if(msg.getStatus().compareTo("Read")==0)
                {
                    //vh2.list_row.setBackgroundColor(Color.TRANSPARENT);
                    vh2.rcv_msg_time.setTextColor(Color.parseColor("#000000"));
                    vh2.rcvdmsg.setTextColor(Color.parseColor("#000000"));
                }

            }

            else if(msg.getContent().compareTo("img")==0)
        {
            vh2.rcvdmsg.setVisibility(View.GONE);
            vh2.rcv_msg_time.setVisibility(View.GONE);
            vh2.rcvdimg.setVisibility(View.VISIBLE);
            vh2.rcvd_img_time.setVisibility(View.VISIBLE);
            Date d = null;
            try {
                d = df.parse(msg.getTime());
                // PrettyTime pt = new PrettyTime(d);
                vh2.rcvd_img_time.setText("Received at " + fm2.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Picasso.with(context).load(msg.getMessage()).into(vh2.rcvdimg);
            if(msg.getStatus().compareTo("Unread")==0)
            {

                //vh2.list_row.setBackgroundColor(Color.parseColor("#73ba4e"));
                vh2.rcvd_img_time.setTextColor(Color.parseColor("#73ba4e"));


//                    vh1.title.setTextColor(Color.parseColor("#00aaff"));
//                    v
            }
            else if(msg.getStatus().compareTo("Read")==0)
            {
                //vh2.list_row.setBackgroundColor(Color.TRANSPARENT);
                vh2.rcvd_img_time.setTextColor(Color.parseColor("#000000"));
            }


        }

            vh2.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.longItemClick(msg,position);
                    return false;
                }

        });
        }

    }

    @Override
    public int getItemCount() {
        return msglist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }




    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        ImageView sentimg;
      //  RelativeLayout list_row;

        TextView sent_msg_time,sent_img_time;


        TextView sentmsg;





        public ViewHolder1(View itemView) {
            super(itemView);
            sentimg = (ImageView) itemView.findViewById(R.id.send_img);
            sentmsg = (TextView) itemView.findViewById(R.id.sent_msg);
            sent_img_time= (TextView) itemView.findViewById(R.id.sent_img_time);
            sent_msg_time= (TextView) itemView.findViewById(R.id.sent_msg_time);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.longItemClick(msg,);
//                }
//            });
         //   list_row= (RelativeLayout) itemView.findViewById(R.id.rcvd_id);





//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //  onDelete();
//                }
//            });
//            comment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onComment();
//                }
//            });

        }

        public void bind(final Message newItem, final onRowItemClick listener) {

//            Picasso.with(itemView.getContext()).load(newItem.getImageid()).into(msgimg);
//            name.setText(newItem.fname);
//            msgtext.setText(newItem.getMsg());
//
//            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
//            // msgtext.setText(newItem);
//            //  Log.d("date from object",newItem.time);
//            Date d = null;
//            try {
//                d = df.parse(newItem.time);
//                PrettyTime pt = new PrettyTime(d);
//                //  Log.d("pretty time",pt.toString());
//                //cTime.setText(pt.format(new Date()));
//                date.setText(pt.format(new Date()));
//                //     cTime.setText(mlist.get(position).time);
////                } catch (ParseException e) {
////                    e.printStackTrace();
////                }
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//

        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {

       // private ImageView ivExample;
       RelativeLayout list_row;
       ImageView rcvdimg;
        TextView rcvdmsg;
        TextView rcv_msg_time;
        TextView rcvd_img_time;
        public ViewHolder2(View v) {
            super(v);
           // ivExample = (ImageView) v.findViewById(R.id.ivExample);
            rcvdmsg = (TextView) itemView.findViewById(R.id.msg_txt);
            rcvdimg = (ImageView) itemView.findViewById(R.id.msg_img);
            rcv_msg_time= (TextView) itemView.findViewById(R.id.rcvd_msg_time);
            rcvd_img_time= (TextView) itemView.findViewById(R.id.rcvd_img_time);
            list_row= (RelativeLayout) itemView.findViewById(R.id.rcvd2_id);

        }


    }



    public interface onRowItemClick {
        void itemClick(Message msg);
        void longItemClick(Message msg,int position);
    }

}
