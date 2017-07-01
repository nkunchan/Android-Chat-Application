package com.example.homework7;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class InboxActivity extends AppCompatActivity implements InboxAdapter.onRowItemClick {
    OwnProfile profile;
    DatabaseReference msgs;
    ArrayList<Inbox_msg> inboxlist;
    RecyclerView rv;
    Inbox_msg msg;
    ValueEventListener msgsli;
    Inbox_msg delete_chat;
    TextView nochats;

    @Override
    protected void onPause() {

        if(msgs!=null&&msgsli!=null)
        msgs.removeEventListener(msgsli);
        super.onPause();
    }

    @Override
    protected void onResume() {

        if(msgs!=null&&msgsli!=null)
        msgs.addValueEventListener(msgsli);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(msgs!=null&&msgsli!=null)
            msgs.removeEventListener(msgsli);

        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
       profile= (OwnProfile) getIntent().getSerializableExtra("ownprofile");
        rv= (RecyclerView) findViewById(R.id.recycle_view);
        nochats= (TextView) findViewById(R.id.no_chats);

       msgs =FirebaseDatabase.getInstance().getReference()
                .child("users").child(profile.getUid()).child("Messages");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        ImageView imageView = new ImageView(actionBar.getThemedContext());
//
//        imageView.setScaleType(ImageView.ScaleType.CENTER);
//        if (profile.getDisplayPic() != null) {
//            Picasso.with(this).load(profile.getDisplayPic()).into(imageView);
//        } else {
//            imageView.setImageResource(R.drawable.empty);
//        }
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.LEFT
//                | Gravity.FILL_VERTICAL);
//        layoutParams.rightMargin = 0;
//        imageView.setLayoutParams(layoutParams);
//        actionBar.setCustomView(imageView);
//        actionBar.setTitle(profile.getDisplayName() + "'s Profile");
        TextView txtv=new TextView(actionBar.getThemedContext());
        txtv.setTextScaleX(2);
        txtv.setText(profile.getDisplayName() + "'s Profile");
        ActionBar.LayoutParams layoutParams2 = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.FILL_VERTICAL);

        txtv.setLayoutParams(layoutParams2);
        actionBar.setCustomView(txtv);

        final DatabaseReference otherUser=FirebaseDatabase.getInstance().getReference()
                .child("users");
       msgsli= msgs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inboxlist=new ArrayList<Inbox_msg>();

                HashMap<String, Object> uids = (HashMap<String, Object>) dataSnapshot.getValue();
                if(uids!=null)
                    for (String uid : uids.keySet()) {

                        HashMap<String, Object> msgs = (HashMap<String, Object>) uids.get(uid);
                        final ArrayList<Inbox_msg> whole_list = new ArrayList<Inbox_msg>();
                        for (String msgid : msgs.keySet()) {

                            HashMap<String, String> single_msg = (HashMap<String, String>) msgs.get(msgid);
                            if (single_msg.get("type").toString().compareTo("received") == 0) {

                                msg = new Inbox_msg();
                                msg.setUid(uid);
                                msg.setTime(single_msg.get("time").toString());
                                msg.setDp(single_msg.get("dp"));
                                msg.setDis_name(single_msg.get("display_name"));
                                msg.setStatus(single_msg.get("status"));

                                msg.setType(single_msg.get("type").toString());
                                msg.setContent(single_msg.get("content").toString());
                                msg.setMessage(single_msg.get("message"));
                                if(msg!=null)
                                    whole_list.add(msg);

                            }




                        }
                        // Log.d("wholelist", whole_list.toString());
                        if(whole_list!=null&&!whole_list.isEmpty())
                        Collections.sort(whole_list, new Comparator<Inbox_msg>() {
                            @Override
                            public int compare(Inbox_msg o1, Inbox_msg o2) {
                                //int result=o1.getNewstitle().compareTo(o2.getNewstitle());
                                SimpleDateFormat fm1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
                                int result = 0;
                                try {
                                    if(o1.getTime()!=null&&o2.getTime()!=null)
                                    result = fm1.parse(o1.getTime()).compareTo(fm1.parse(o2.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (result > 0)
                                    return -1;
                                else if (result < 0)
                                    return 1;
                                else
                                    return 0;


                            }
                        });
                        if (whole_list != null && whole_list.size()!=0)
                            inboxlist.add(whole_list.get(0));



                    }
              //  Log.d("inboxlist", inboxlist.toString());



                if(inboxlist!=null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(InboxActivity.this, LinearLayoutManager.VERTICAL, false);

                    //Log.d("chatlist", inboxlist.toString());
                    final InboxAdapter fadap1 = new InboxAdapter(InboxActivity.this, inboxlist,InboxActivity.this);
                    rv.setLayoutManager(layoutManager);
                    rv.setAdapter(fadap1);
                    if(inboxlist.isEmpty())
                    {
                        nochats.setText("No chats to show");
                    }
                }
                else if(inboxlist==null||inboxlist.isEmpty())
                {
                    nochats.setText("No chats to show");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    @Override
    public void onItemClick(Inbox_msg item, int position) {
        OtherUsers sender=new OtherUsers();
        sender.setDp(item.getDp());
        sender.setUid(item.getUid());
        sender.setDisplayName(item.getDis_name());

        Intent i=new Intent(InboxActivity.this,ChatActivity.class);
        i.putExtra("selecteduser",sender);
        i.putExtra("ownprofile",profile);
        startActivity(i);



    }

    @Override
    public void onLongClick(Inbox_msg item, int position) {

         delete_chat=item;

        AlertDialog.Builder alertbox= new AlertDialog.Builder(InboxActivity.this);
        alertbox.setMessage("Remove Chat?");
        alertbox.setCancelable(false);
        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference deletechat = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(profile.getUid()).child("Messages").child(delete_chat.getUid());
                deletechat.removeValue();

            }

        });
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertbox.create();
        alertDialog.show();


    }





    }

