package com.example.homework7;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.onRowItemClick {
    OtherUsers selecteduser;
    OwnProfile profile;
    ImageView sendtxt, sendimg;
    EditText txtmsg;
    RecyclerView rv;
    final static int SELECTED_PICTURE = 1;
    final int REQUEST_WRITE_PERMISSION = 786;
    Message deleted_msg;
    DatabaseReference msgref1;
    ValueEventListener status_change;
    ProgressDialog progress;

    @Override
    protected void onResume() {
        super.onResume();
       // if(msgref1!=null&&status_change!=null)
      //  msgref1.removeEventListener(status_change);

    }

    @Override
    protected void onStop() {
       // if(msgref1!=null&&status_change!=null)
         //   msgref1.removeEventListener(status_change);

        super.onStop();

    }
    protected void remove_color()
    {
        msgref1 = FirebaseDatabase.getInstance().getReference()
                .child("users").child(profile.getUid()).child("Messages").child(selecteduser.getUid());



        msgref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> msgs= (HashMap<String, Object>) dataSnapshot.getValue();
                if(msgs!=null)
                {
                    for(String msgid:msgs.keySet())
                    {
                        HashMap<String,Object> singlemsg3= (HashMap<String, Object>) msgs.get(msgid);
                        // Log.d("singlemsgcolor",singlemsg3.toString());
                        if(singlemsg3!=null&&singlemsg3.get("type").toString().compareTo("received")==0&&singlemsg3.get("status").toString().compareTo("Unread")==0) {
                            Log.d("unread","to read");
                            OtherUsers selecteduser1=(OtherUsers) getIntent().getSerializableExtra("selecteduser");
                            if(singlemsg3.get("display_name").toString().compareTo(selecteduser1.getDisplayName().toString())==0) {
                                Log.d("msg received by other",selecteduser1.getDisplayName().toString());
                                msgref1.child(msgid).child("status").setValue("Read");
                            }

                        }
                    }
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        selecteduser = (OtherUsers) getIntent().getSerializableExtra("selecteduser");
        profile = (OwnProfile) getIntent().getSerializableExtra("ownprofile");
        ImageView dp = (ImageView) findViewById(R.id.user_dp);
        if (selecteduser.getDp() != null)
            Picasso.with(this).load(selecteduser.getDp()).into(dp);
        TextView displayname = (TextView) findViewById(R.id.display_name);
        displayname.setText(selecteduser.getDisplayName());
        Log.d("profile", selecteduser.toString());
        rv = (RecyclerView) findViewById(R.id.recycle_view);
        sendtxt = (ImageView) findViewById(R.id.send_btn);
        txtmsg = (EditText) findViewById(R.id.msg_txt);
        sendimg = (ImageView) findViewById(R.id.send_img);
        DatabaseReference msgref = FirebaseDatabase.getInstance().getReference()
                .child("users").child(profile.getUid()).child("Messages").child(selecteduser.getUid());
        Log.d("hi","on back in chat");

        Log.d("pro file",profile.getDisplayName());
        Log.d("other user",selecteduser.getDisplayName());
        msgref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  Log.d("msg snapshot", dataSnapshot.toString());
                ArrayList<Message> chatlist = new ArrayList<Message>();

                HashMap<String, Object> msgs = (HashMap<String, Object>) dataSnapshot.getValue();
                if(msgs!=null)
                    for (String msgid : msgs.keySet()) {
                        Message msg = new Message();
                        HashMap<String, String> singlemsg = (HashMap<String, String>) msgs.get(msgid);
                        msg.setTime(singlemsg.get("time").toString());

                        msg.setType(singlemsg.get("type").toString());
                        msg.setContent(singlemsg.get("content").toString());
                        msg.setDisplay_name(selecteduser.getDisplayName());
                        msg.setDp(selecteduser.getDp());
                        msg.setStatus(singlemsg.get("status"));

                        msg.setMessage(singlemsg.get("message"));
                        chatlist.add(msg);



                    }
                Collections.sort(chatlist, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        //int result=o1.getNewstitle().compareTo(o2.getNewstitle());
                        SimpleDateFormat fm1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
                        int result = 0;
                        try {
                            result = fm1.parse(o1.getTime()).compareTo(fm1.parse(o2.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (result > 0)
                            return 1;
                        else if (result < 0)
                            return -1;
                        else
                            return 0;


                    }
                });
//
                if(chatlist!=null) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);

                    //Log.d("chatlist", chatlist.toString());
                    final ChatAdapter fadap1 = new ChatAdapter(ChatActivity.this, chatlist,ChatActivity.this);
                    rv.setLayoutManager(layoutManager);
                    rv.setAdapter(fadap1);
                    if(chatlist.size()!=0)
                        //rv.smoothScrollToPosition(chatlist.size()-1);
                        layoutManager.scrollToPositionWithOffset(chatlist.size()-1, 0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_color();
                Message msg = new Message();
                Message msg1 = new Message();
                msg.setMessage(txtmsg.getText().toString());
                msg.setDp(profile.getDisplayPic());
                msg.setDisplay_name(profile.getDisplayName());
                msg1.setMessage(txtmsg.getText().toString());
                Date d = new Date();
                msg.setTime(d.toString());
                msg1.setTime(d.toString());
                msg.setType("received");
                msg1.setType("sent");
                msg1.setDisplay_name(selecteduser.getDisplayName());
                msg1.setDp(selecteduser.getDp());
                msg.setContent("msg");
                msg1.setContent("msg");
                msg.setStatus("Unread");

                FirebaseDatabase.getInstance().getReference()
                        .child("users").child(selecteduser.getUid()).child("Messages").child(profile.getUid()).push().setValue(msg);
                FirebaseDatabase.getInstance().getReference()
                        .child("users").child(profile.getUid()).child("Messages").child(selecteduser.getUid()).push().setValue(msg1);

                txtmsg.setText("");


            }
        });

        sendimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                remove_color();
                requestPermission();




            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "picture"), SELECTED_PICTURE);


        }
    }
//
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {

            Intent takepicture = new Intent(Intent.ACTION_PICK);
            takepicture.setType("image/*");
            startActivityForResult(takepicture, SELECTED_PICTURE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECTED_PICTURE:
                if (resultCode == RESULT_OK) {
                    ImageView image = new ImageView(this);
                    final Uri uri = data.getData();

                    image.setImageURI(uri);

                    AlertDialog.Builder alertadd = new AlertDialog.Builder(
                            ChatActivity.this);
                    LayoutInflater factory = LayoutInflater.from(ChatActivity.this);
////                    final View view = factory.inflate(R.layout.select_img_layout, null);
////                       view.setim
                    alertadd.setView(image);
                    alertadd.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {
                            progress = new ProgressDialog(ChatActivity.this);
                            progress.setMessage("Sending Image :) ");
                            // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progress.setIndeterminate(true);

                            progress.show();
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference mStorage = storage.getReferenceFromUrl("gs://homework7-d38c4.appspot.com");

                            StorageReference sender_path = mStorage.child("Users").child(profile.getUid()).child(selecteduser.getUid());
                            sender_path.child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    Message msg1 = new Message();
                                    msg1.setMessage(taskSnapshot.getDownloadUrl().toString());
                                    Date d = new Date();
                                    msg1.setTime(d.toString());
                                    msg1.setDisplay_name(selecteduser.getDisplayName());
                                    msg1.setDp(selecteduser.getDp());
                                    msg1.setType("sent");
                                    msg1.setContent("img");
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(profile.getUid()).child("Messages").child(selecteduser.getUid()).push().setValue(msg1);
                                    progress.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this,"Failed to load image",Toast.LENGTH_SHORT).show();

                                }
                            });
                            StorageReference receiver_path = mStorage.child("Users").child(selecteduser.getUid()).child(profile.getUid());
                            receiver_path.child(uri.getLastPathSegment()+"receive").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Message msg = new Message();
                                    msg.setMessage(taskSnapshot.getDownloadUrl().toString());
                                    Date d = new Date();
                                    msg.setTime(d.toString());
                                    msg.setType("received");
                                    msg.setStatus("Unread");
                                    msg.setContent("img");
                                    msg.setDp(profile.getDisplayPic());
                                    msg.setDisplay_name(profile.getDisplayName());


                                    FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(selecteduser.getUid()).child("Messages").child(profile.getUid()).push().setValue(msg);
//



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this,"Failed to load image",Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    });
                    alertadd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {


                        }
                    });

                    alertadd.show();


                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        remove_color();

//        msgref1 = FirebaseDatabase.getInstance().getReference()
//                .child("users").child(profile.getUid()).child("Messages").child(selecteduser.getUid());
//
//      status_change=  msgref1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                HashMap<String,Object> msgs= (HashMap<String, Object>) dataSnapshot.getValue();
//                if(msgs!=null)
//                {
//                    for(String msgid:msgs.keySet())
//                    {
//                        HashMap<String,Object> singlemsg= (HashMap<String, Object>) msgs.get(msgid);
//                       // Log.d("singlemsgcolor",singlemsg.toString());
//                        if(singlemsg!=null&&singlemsg.get("type").toString().compareTo("received")==0&&singlemsg.get("status").toString().compareTo("Unread")==0) {
//                            Log.d("unread","to read");
//                            if(singlemsg.get("display_name").toString().compareTo(selecteduser.getDisplayName().toString())==0)
//
//                            msgref1.child(msgid).child("status").setValue("Read");
//
//                        }
//                    }
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        super.onBackPressed();
    }

    @Override
    public void itemClick(Message msg) {

    }

    @Override
    public void longItemClick(Message msg, int position) {
         deleted_msg=msg;
        Log.d("callinglongclick",msg.toString());

        android.app.AlertDialog.Builder alertbox= new android.app.AlertDialog.Builder(ChatActivity.this);
        alertbox.setMessage("Remove message?");
        alertbox.setCancelable(false);
        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference deletemsg = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(profile.getUid()).child("Messages").child(selecteduser.getUid());
                deletemsg.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap<String,Object> msgs= (HashMap<String, Object>) dataSnapshot.getValue();
                        if(msgs!=null)
                        for(String msgid:msgs.keySet())
                        {
                            Message object=new Message();
                            HashMap<String,String> message= (HashMap<String, String>) msgs.get(msgid);

                            object.setStatus(message.get("status"));
                            object.setTime(message.get("time"));
                            object.setDisplay_name(message.get("display_name"));
                            object.setDp(message.get("dp"));
                            object.setMessage(message.get("message"));
                            object.setContent(message.get("content"));
                            object.setType(message.get("type"));
                            Log.d("object",object.toString());
                            if(object.toString().compareTo(deleted_msg.toString())==0)
                            {
                                Log.d("deleting","message");
                                deletemsg.child(msgid).removeValue();
                            }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }

        });
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.app.AlertDialog alertDialog = alertbox.create();
        alertDialog.show();




    }
}
