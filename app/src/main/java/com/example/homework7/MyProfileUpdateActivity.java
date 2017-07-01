package com.example.homework7;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.GetServiceRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class MyProfileUpdateActivity extends AppCompatActivity {
    OwnProfile profile;
    EditText displayname;
    ImageView displaypic;
    ImageView editpic;
    final static int SELECTED_PICTURE = 1;
    final int REQUEST_WRITE_PERMISSION = 786;
    Uri uri;
    RadioGroup rg1;
    RadioButton rb;
    String radioval;
    StorageReference mStorage;
    DatabaseReference mPhotos;
    DatabaseReference mPhotobyID;
    DatabaseReference mPhotoLink;
    DatabaseReference mImgUploadDate;
    Uri downloadurl;
    TextView gender;
    OwnProfile prf;
    TextView nm;
    String pkey;
    ImageView removepic;
    ProgressDialog progress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_update);
        setTitle("View/Update Profile");
        pkey = getIntent().getExtras().getString("pkey1");
        profile = (OwnProfile) getIntent().getExtras().getSerializable("userprofile");
        mStorage = FirebaseStorage.getInstance().getReference();
        rg1 = (RadioGroup) findViewById(R.id.rg1);
        rg1.setVisibility(View.GONE);
        gender = (TextView) findViewById(R.id.txtGen);
        if (profile.getGender() != null) {
            gender.setText(profile.getGender());
        } else {
            gender.setText("Not Specified");
        }

//        Log.d("getgender", profile.getGender());
        gender.setVisibility(View.VISIBLE);
        //nm = (TextView) findViewById(R.id.txtnm);
        displayname = (EditText) findViewById(R.id.editName);
        displayname.setText(profile.getDisplayName());
        displayname.setVisibility(View.VISIBLE);
        //nm.setText(profile.displayName);

        //displayname.setVisibility(View.GONE);
        removepic = (ImageView) findViewById(R.id.imageView10);
        removepic.setVisibility(View.GONE);
        displaypic = (ImageView) findViewById(R.id.imageView4);
        if (profile.displayPic != null) {
            removepic.setVisibility(View.VISIBLE);
            Picasso.with(this).load(profile.displayPic).into(displaypic);
        } else if (profile.displayPic == null) {

            displaypic.setImageResource(R.drawable.empty);
            removepic.setVisibility(View.GONE);
        }

        editpic = (ImageView) findViewById(R.id.imageView7);
        editpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                prf = new OwnProfile();
                                                                StorageReference mPhotoPath = mStorage.child("Photos").child("DP" + profile.uid);
                                                                if (uri != null) {
                                                                    progress1 = new ProgressDialog(MyProfileUpdateActivity.this);
                                                                    progress1.setMessage("Updating Profile.. ");
                                                                    // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                                    progress1.setIndeterminate(true);
                                                                    progress1.show();
                                                                    mPhotoPath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                          //  Toast.makeText(MyProfileUpdateActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                                                                            downloadurl = taskSnapshot.getDownloadUrl();
                                                                            if (downloadurl != null) {
                                                                                FirebaseDatabase.getInstance().getReference().child("users").child(profile.uid).child("ProfilePic")
                                                                                        .setValue(downloadurl.toString());
                                                                                Log.d("setdown", downloadurl.toString());
                                                                                progress1.dismiss();
                                                                            }

                                                                        }

                                                                    });
                                                                }

                                                                FirebaseDatabase.getInstance().getReference().child("users").child(profile.uid).child("DisplayName")
                                                                        .setValue(displayname.getText().toString());
                                                                FirebaseDatabase.getInstance().getReference().child("users").child(profile.uid).child("Gender")
                                                                        .setValue(radioval);

                                                                  FirebaseDatabase.getInstance().getReference().child("users").child(profile.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              OwnProfile profile1= new OwnProfile();

                HashMap<String,String> updated=(HashMap<String,String> )dataSnapshot.getValue();
                profile1.setDisplayName(updated.get("DisplayName"));
               if(updated.get("ProfilePic")!=null)
                profile1.setDisplayPic(updated.get("ProfilePic"));
                profile1.setGender(updated.get("Gender"));
                profile1.setUid(profile.getUid());

                Intent i = new Intent(MyProfileUpdateActivity.this, GetConnected.class);
                i.putExtra("ownprofile", profile1);
                startActivity(i);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//                                                                prf.setGender(radioval);
//                                                                prf.getDisplayPic(


//                    FirebaseDatabase.getInstance().getReference().child("users").child(profile.uid).child("ProfilePic")
//                            .setValue(downloadurl.toString());

//                    Log.d("printdowmloadlasseg", downloadurl.getLastPathSegment());

                                                            }
                                                        }

        );
        // prf.setDisplayName(displayname.getText().toString());


        removepic.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                             //   Log.d("pkey", pkey);

                                             if (pkey != null) {
                                                 Toast.makeText(MyProfileUpdateActivity.this, "DO Not", Toast.LENGTH_LONG).show();
                                             }
                                             AlertDialog.Builder alertbox = new AlertDialog.Builder(MyProfileUpdateActivity.this);

                                             alertbox.setMessage("Remove Profile Picture?");
                                             alertbox.setCancelable(false);
                                             alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     StorageReference mPhotoPath = mStorage.child("Photos").child("DP" + profile.uid);
                                                     Log.d("storageref", mPhotoPath.toString());
                                                     mPhotoPath.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                         @Override
                                                         public void onSuccess(Void aVoid) {
                                                             Toast.makeText(MyProfileUpdateActivity.this, "Profile Picture Removed", Toast.LENGTH_LONG).show();
                                                             displaypic.setImageResource(R.drawable.empty);
                                                             FirebaseDatabase.getInstance().getReference().child("users").child(profile.uid).child("ProfilePic")
                                                                     .setValue(null);
                                                             //  removepic.setVisibility(View.GONE);


                                                         }
                                                     }).addOnFailureListener(new OnFailureListener() {
                                                         @Override
                                                         public void onFailure(@NonNull Exception e) {
                                                             Toast.makeText(MyProfileUpdateActivity.this, "Error Encountered. Please try again.", Toast.LENGTH_LONG).show();

                                                         }
                                                     });


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

        );


        findViewById(R.id.btncancel)

                .

                        setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   finish();
                                               }
                                           }

                        );

        findViewById(R.id.imageView6)

                .

                        setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   rg1.setVisibility(View.VISIBLE);
                                                   gender.setVisibility(View.GONE);
                                                   findViewById(R.id.imageView6).setVisibility(View.GONE);
                                                   rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                       @Override
                                                       public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                           rb = (RadioButton) findViewById(checkedId);
                                                           radioval = (String) rb.getText();
                                                       }
                                                   });


                                               }
                                           }

                        );

    }

    //for image

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
            //openFilePicker();
            Intent takepicture = new Intent(Intent.ACTION_PICK);
            takepicture.setType("image/*");
            startActivityForResult(takepicture, SELECTED_PICTURE);

        }
    }
    //


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTED_PICTURE && resultCode == RESULT_OK) {
            uri = data.getData();
            displaypic.setImageURI(uri);
            Log.d("oprinturllastseg", uri.getLastPathSegment());
            //2132196866


        }
    }
}