package com.example.homework7;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class OtherUserDetailActivity extends AppCompatActivity {
    String name;
    String dp;
    String id;
    DatabaseReference mDatabase;
    DatabaseReference mUserRefByID;
    DatabaseReference mPhotos;
    GridView mygrid;
    GVAdapter gadapter;
    TextView lbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_detail);
        name = getIntent().getExtras().getString("name");
        if (getIntent().getExtras().getString("DisplayPicture") != null)
            dp = getIntent().getExtras().getString("DisplayPicture");
        //Log.d("getdp", dp);
        id = getIntent().getExtras().getString("id");
        Log.d("showmname", name);
        lbl = (TextView) findViewById(R.id.txtlbl);
        String name2 = name;
        int spaceIndex = name2.indexOf(" ");
        String name3 = name2.substring(0, spaceIndex);
        lbl.setText(name3 + "'s Photos");
        //setTitle(name+"'s Profile");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserRefByID = mDatabase.child("users").child(id);
        mPhotos = mUserRefByID.child("PhotosUploadedbyOnwer");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()|ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        TextView txtv=new TextView(actionBar.getThemedContext());
        txtv.setTextScaleX(2);
        txtv.setText(name + "'s Profile");
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        if (dp != null) {
            Picasso.with(this).load(dp).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.empty);
        }
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.FILL_VERTICAL);
        layoutParams.rightMargin = 20;
        imageView.setLayoutParams(layoutParams);
        ActionBar.LayoutParams layoutParams2 = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.FILL_VERTICAL);

        txtv.setLayoutParams(layoutParams2);
        actionBar.setCustomView(imageView);
        actionBar.setCustomView(txtv);
       // actionBar.setTitle(name + "'s Profile");

//menu





        mPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserImages> imglist = new ArrayList<UserImages>();
                HashMap<String, Object> res1 = (HashMap<String, Object>) dataSnapshot.getValue();
                if (res1 != null) {
                    for (String imgid : res1.keySet()) {
                        HashMap<String, String> res2 = (HashMap<String, String>) res1.get(imgid);
                        UserImages img = new UserImages();
                        img.setDateofupload(res2.get("UploadDate"));
                        img.setImage(res2.get("PhotoLink"));
                        imglist.add(img);
                    }
                    //code to set Adapter
                    mygrid = (GridView) findViewById(R.id.gd);
                    if(!imglist.isEmpty() && imglist!=null){
                    gadapter = new GVAdapter(OtherUserDetailActivity.this, imglist, R.layout.item_grid_layout);}
                    mygrid.setAdapter(gadapter);
                } else if (res1 == null) {

                    //
                    LinearLayout linearLayout = new LinearLayout(OtherUserDetailActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

//ImageView Setup
                    ImageView imageView = new ImageView(OtherUserDetailActivity.this);

//setting image resource
                    imageView.setImageResource(R.drawable.nopic);

//setting image position
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

//adding view to layout
                    linearLayout.addView(imageView);
//make visible to program
                    setContentView(linearLayout);
                    ///

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //menu function
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.back:
                finish();
                break;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OtherUserDetailActivity.this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }
}
