package com.example.homework7;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class GetConnected extends AppCompatActivity implements ImageAdapterForOwn.onRowItemClick{
    Button sendmsg;
    DatabaseReference usertReference;
    Button logout;
    HashMap<String, Object> result;
    OwnProfile profile;
    ArrayList<OtherUsers> list2;
    StorageReference mStorage;
    static final int GALLERY_INTENT = 2;
    Uri downloadurl;
    DatabaseReference mDatabase;
    DatabaseReference mPhotos;
    DatabaseReference mPhotobyID;
    DatabaseReference mPhotoLink;
    DatabaseReference mImgUploadDate;
    DatabaseReference unreadref;
    ArrayList<UserImages> imagelist;
    HashMap<String, Object> res1;
    RecyclerView rv;
    OwnProfile prf;
    LinearLayoutManager lytmanager;
    ImageAdapterForOwn fadap2;
    TextView noAlbumLabel;
    ImageView noAlbumIcon;
    ImageView   viewOthersProfile;
    ImageView inbox;
    final static int SELECTED_PICTURE = 1;
    final int REQUEST_WRITE_PERMISSION = 786;
    AlertDialog alert;
    TextView unreadmsg;
    ValueEventListener event1,event2,event3;
    ProgressDialog progress;

    ImageView dp;

    @Override
    protected void onStop() {
        if(unreadref!=null&&event1!=null)
        {
            unreadref.removeEventListener(event1);
        }
        if(usertReference!=null&&event2!=null)
        {
            usertReference.removeEventListener(event2);
        }
        if(mPhotos!=null&&event3!=null)
        {
            mPhotos.removeEventListener(event3);

        }
        super.onStop();
    }

    @Override
    protected void onPause() {

        if(unreadref!=null&&event1!=null)
        {
            unreadref.removeEventListener(event1);
        }
        if(usertReference!=null&&event2!=null)
        {
            usertReference.removeEventListener(event2);
        }
        if(mPhotos!=null&&event3!=null)
        {
            mPhotos.removeEventListener(event3);

        }
        super.onPause();





    }

    @Override
    protected void onResume() {

        if(unreadref!=null&&event1!=null)
        {
            unreadref.addValueEventListener(event1);
        }
        if(usertReference!=null&&event2!=null)
        {
            usertReference.addValueEventListener(event2);
        }
        if(mPhotos!=null&&event3!=null)
        {
            mPhotos.addValueEventListener(event3);

        }



        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_connected);
        setTitle("Get Connected-Welcome Page");
        mStorage = FirebaseStorage.getInstance().getReference();
        sendmsg = (Button) findViewById(R.id.btnNewMessage);
        inbox= (ImageView) findViewById(R.id.inbox);
        rv = (RecyclerView) findViewById(R.id.rvforUser);
        noAlbumIcon = (ImageView) findViewById(R.id.imgNoPic);
        noAlbumLabel = (TextView) findViewById(R.id.txtnoPhotos);
        noAlbumIcon.setVisibility(View.GONE);
        noAlbumLabel.setVisibility(View.GONE);
        viewOthersProfile= (ImageView) findViewById(R.id.others);
        profile = (OwnProfile) getIntent().getSerializableExtra("ownprofile");
        findViewById(R.id.my_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetConnected.this, MyProfileUpdateActivity.class);
                i.putExtra("userprofile", prf);
               // i.putExtra("pkey1",pkey);
                startActivity(i);
            }
        });


        Log.d("in getconnected","after inobx");

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(GetConnected.this,InboxActivity.class);
                i.putExtra("ownprofile",profile);
                startActivity(i);
            }
        });

//        FirebaseDatabase.getInstance().getReference().child("users").child(profile.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//               profile= new OwnProfile();
//
//                HashMap<String,String> updated=(HashMap<String,String> )dataSnapshot.getValue();
//                profile.setDisplayName(updated.get("DisplayName"));
//               if(updated.get("ProfilePic")!=null)
//                profile.setDisplayPic(updated.get("ProfilePic"));
//                profile.setGender(updated.get("Gender"));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




         dp = (ImageView) findViewById(R.id.imgViewDisplayPicture);
        if(profile.getDisplayPic()!=null)
        Picasso.with(this).load(profile.getDisplayPic()).into(dp);
        TextView displayname = (TextView) findViewById(R.id.txtdisplayName);
        displayname.setText("Welcome " + profile.getDisplayName());


        // no of unread messages
        Log.d("profileuid",profile.getUid());
         unreadref=FirebaseDatabase.getInstance().getReference().child("users").child(profile.getUid()).child("Messages");
       event1= unreadref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter=0;
                HashMap<String,Object> uids= (HashMap<String, Object>) dataSnapshot.getValue();

                if(uids!=null)
                {
                    for(String uid:uids.keySet())
                    {
                        HashMap<String,Object> single_uid= (HashMap<String, Object>) uids.get(uid);

                        for(String msgid:single_uid.keySet())
                        {
                            HashMap<String,String> single_msg= (HashMap<String, String>) single_uid.get(msgid);



                            if(single_msg.containsKey("status"))
                        {
                            Log.d("singlemsg",single_msg.toString());
                            if(single_msg.get("type").toString().compareTo("received")==0&&single_msg.get("status").toString().compareTo("Unread")==0)
                            {
                                Log.d("singlemsgunread",single_msg.toString());
                                counter++;
                            }
                        }
                        }


                    }
                    unreadmsg= (TextView) findViewById(R.id.unread);
                    unreadmsg.setText("");
                    if(counter!=0)
                    unreadmsg.setText(String.valueOf(counter));

                    Log.d("counter",String.valueOf(counter));
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        usertReference = FirebaseDatabase.getInstance().getReference()
                .child("users");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPhotos = mDatabase.child("users").child(profile.getUid()).child("PhotosUploadedbyOnwer");


       event2= usertReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result = (HashMap<String, Object>) dataSnapshot.getValue();
                if (result != null) {

                    ArrayList<OtherUsers> list1 = new ArrayList<>();
                    for (String uid : result.keySet()) {
                        if (uid.compareTo(profile.getUid())!=0) {
                            HashMap<String, Object> result1 = (HashMap<String, Object>) result.get(uid);
                            //  result1= (HashMap<String, Object>) result1.remove(profile.getUid());
                            OtherUsers user = new OtherUsers();
                            if (result1 != null) {
                                if(result1.get("DisplayName")!=null)
                                user.displayName = result1.get("DisplayName").toString();
                                if (result1.get("ProfilePic") != null)
                                    user.setDp(result1.get("ProfilePic").toString());

                                user.setUid(uid);


                                // User user= (User) result.get(userid);
                                list1.add(user);


                 //               Log.d("mapvalue", result1.get("DisplayName").toString());
                                //   Log.d("mapname", result1.get("ProfilePic").toString());


//                        exp.setAdapter(fapad1);
//                        fapad1.setNotifyOnChange(true);
                            }
                        }
                        else if(uid.compareTo(profile.getUid())==0){
                            HashMap<String, String> result2 = (HashMap<String, String>) result.get(uid);
                            prf=new OwnProfile();
                            if(result2.containsKey("ProfilePic")){
                                Picasso.with(GetConnected.this).load(result2.get("ProfilePic").toString()).into(dp);
                                prf.setDisplayPic(result2.get("ProfilePic"));
                            }
                            else{
                                dp.setImageResource(R.drawable.empty);

                            }
                            prf.setDisplayName(result2.get("DisplayName"));
                            prf.setGender(result2.get("Gender"));
                            prf.setUid(uid);}
                    }


                    list2=list1;
                    Log.d("result is", result.toString());
                    Log.d("list1 is", list1.toString());


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //recycler View setUp

       event3= mPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagelist = new ArrayList<UserImages>();
                //if(dataSnapshot.toString()!=null)
                Log.d("getvalue", dataSnapshot.toString());
                res1 = (HashMap<String, Object>) dataSnapshot.getValue();
                if (res1 != null) {
                    for (String imgid : res1.keySet()) {
                        HashMap<String, String> res2 = (HashMap<String, String>) res1.get(imgid);
                        UserImages img2 = new UserImages();
                        img2.setImage(res2.get("PhotoLink"));
                        img2.setDateofupload(res2.get("UploadDate"));
                        imagelist.add(img2);
                    }
                    rv.setVisibility(View.VISIBLE);
                    Log.d("listcontent", imagelist.toString());
                    lytmanager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    fadap2 = new ImageAdapterForOwn(GetConnected.this, imagelist, GetConnected.this);
                    rv.setLayoutManager(lytmanager);
                    rv.setAdapter(fadap2);
                    noAlbumIcon.setVisibility(View.GONE);
                    noAlbumLabel.setVisibility(View.GONE);
////

                } else if (res1 == null) {
                    if (imagelist.isEmpty()) {
                        noAlbumIcon.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        noAlbumLabel.setVisibility(View.VISIBLE);
                    }
                }



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//ennd of listener


        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GetConnected.this);
                OtherUsersAdapter fapad1 = new OtherUsersAdapter(GetConnected.this, R.layout.other_users, list2);
                // Log.d("list2",list2.toString());
                alertDialogBuilder.setTitle("Select Contact").setAdapter(fapad1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("selected item",list2.get(which).toString());

                        alert.dismiss();
                        Intent i=new Intent(GetConnected.this,ChatActivity.class);
                        i.putExtra("selecteduser", (Serializable) list2.get(which));
                        i.putExtra("ownprofile",profile);
                        startActivity(i);



                    }
                });
                if (list2 != null) {
                    alert = alertDialogBuilder.create();
                    alert.show();
                }

            }
        });

        //UploadImagetoStorage
        findViewById(R.id.btnUploadImages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, GALLERY_INTENT);
//                progress = new ProgressDialog(GetConnected.this);
//                progress.setMessage("uploading Image :) ");
//                // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                progress.setIndeterminate(true);
//                progress.show();

                requestPermission();


            }
        });

        findViewById(R.id.imgLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // GoogleApiClient gclient;
                if(LoginManager.getInstance()!=null)

                    LoginManager.getInstance().logOut();

//                if()
//                {
//                    Auth.GoogleSignInApi.signOut();
//                }
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(GetConnected.this, LoginActivity.class);
                startActivity(i);

            }
        });


        viewOthersProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
                AlertDialog.Builder alertbox=new AlertDialog.Builder(GetConnected.this);
                alertbox.setTitle("Select Contact").setIcon(R.drawable.com_facebook_profile_picture_blank_square);
                OtherUsersAdapter fapad1 = new OtherUsersAdapter(GetConnected.this, R.layout.other_users, list2);
                alertbox.setAdapter(fapad1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     Intent i=new Intent(GetConnected.this,OtherUserDetailActivity.class);
                        i.putExtra("name",list2.get(which).displayName);
                        if(list2.get(which).dp!=null)
                        i.putExtra("DisplayPicture",list2.get(which).dp);
                        i.putExtra("id",list2.get(which).uid);
                        startActivity(i);

                    }
                });
                if (list2 != null) {
                    AlertDialog alert = alertbox.create();
                    alert.show();
                }

            }
        });
    }//end of onCreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTED_PICTURE && resultCode == RESULT_OK) {
            progress = new ProgressDialog(GetConnected.this);
            progress.setMessage("uploading Image :) ");
            // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();

            final Uri uri = data.getData();

            StorageReference mPhotoPath = mStorage.child("Photos").child(uri.getLastPathSegment());
            mPhotoPath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(GetConnected.this, "Upload Done", Toast.LENGTH_LONG).show();
                    downloadurl = taskSnapshot.getDownloadUrl();
                    mPhotobyID = mPhotos.child("Photo" + UUID.randomUUID());
                    mPhotoLink = mPhotobyID.child("PhotoLink");
                    mImgUploadDate = mPhotobyID.child("UploadDate");
                    mPhotoLink.setValue(downloadurl.toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
                    Date d1 = new Date();
                    String d2 = sdf.format(d1);
                    mImgUploadDate.setValue(d2);
                    progress.dismiss();


                }
            });

        }

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
    public void onClick(UserImages img5) {
        Intent i = new Intent(GetConnected.this, DisplayOwnImageActivity.class);
        i.putExtra("image", img5);
        i.putExtra("wholelist", imagelist);
        startActivity(i);
    }

    @Override
    public void onDeleteClick(UserImages img3) {
        if (res1 != null) {

            for (String imgid1 : res1.keySet()) {

                HashMap<String, String> res3 = (HashMap<String, String>) res1.get(imgid1);
                Log.d("res3", res3.toString());

                UserImages img = new UserImages();
                img.setImage(res3.get("PhotoLink"));
                img.setDateofupload(res3.get("UploadDate"));
                if (img.toString().compareTo(img3.toString()) == 0) {
                    mPhotos.child(imgid1).removeValue();
                    imagelist.remove(img3);
                }
            }
        }

    }
}
