package com.example.homework7;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplayOwnImageActivity extends AppCompatActivity {
    TextView uploadate;
    ImageView uploadedImage;
    Button gobackbtn;
    UserImages img;
    ArrayList<UserImages> allImglist;
    Bundle b;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_own_image);
        setTitle("My Album");
        gobackbtn = (Button) findViewById(R.id.btngoback);
        uploadedImage = (ImageView) findViewById(R.id.imgFull);
        uploadate = (TextView) findViewById(R.id.dateOfCreation);
        img = (UserImages) getIntent().getExtras().getSerializable("image");
      //  allImglist = (ArrayList<UserImages>) b.getSerializableEx("wholelist");
        allImglist = (ArrayList<UserImages>) getIntent().getSerializableExtra("wholelist");
        Log.d("getlistindisplay", allImglist.toString());
        Picasso.with(this).load(img.getImage()).into(uploadedImage);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
        String date1 = img.getDateofupload();
        try {
            Date d1 = sdf.parse(date1);
            PrettyTime pt = new PrettyTime(d1);
            uploadate.setText("Uploaded " + pt.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        gobackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.imgPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0)
                    count = allImglist.size() - 1;
                else
                    count--;
               Picasso.with(DisplayOwnImageActivity.this).load(allImglist.get(count).getImage()).into(uploadedImage);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
                String date1 = allImglist.get(count).dateofupload;
                try {
                    Date d1 = sdf.parse(date1);
                    PrettyTime pt = new PrettyTime(d1);
                    uploadate.setText("Uploaded " + pt.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == allImglist.size() - 1)
                    count = 0;
                else
                    count++;
                Picasso.with(DisplayOwnImageActivity.this).load(allImglist.get(count).getImage()).into(uploadedImage);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
                String date1 = allImglist.get(count).dateofupload;
                try {
                    Date d1 = sdf.parse(date1);
                    PrettyTime pt = new PrettyTime(d1);
                    uploadate.setText("Uploaded " + pt.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
