package com.example.homework7;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText mEmail;
    EditText mPassword;
    Button mLogin;
    LoginButton loginfb;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    Task<Void> usertReference;
    SignInButton signInbtn;
    Button signoutbtn;
    GoogleApiClient gclient;
    TextView displaymsg;
    private static final String TAG = "googlesignin";
    private static final int RC_SIGN_IN = 9001;
    String fname, photo;
    DatabaseReference loginref,loginref1;
    ValueEventListener loginlist,loginlist2;
    ProgressDialog progress;
    ProgressDialog progress1;
    int login = 0;
    String photo1=null,fname1;
    private CallbackManager mCallbackManager;

    @Override
    protected void onPause() {
        super.onPause();
        if (loginref != null && loginlist != null)
            loginref.removeEventListener(loginlist);
     //   if(loginlist2!=null&&loginref1!=null)
       //     loginref1.removeEventListener(loginlist2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Get Connected(Login)");

        //
        FacebookSdk.sdkInitialize(getApplicationContext());
       // AppEventsLogger.activateApp(this);
        LoginManager.getInstance().logOut();

     //   signoutbtn = (Button) findViewById(R.id.logout);
        login = 0;
        mEmail = (EditText) findViewById(R.id.editText);
        mPassword = (EditText) findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();

        //google
        signInbtn = (SignInButton) findViewById(R.id.btn);
        final GoogleSignInOptions goptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, goptions).build();



        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(gclient);
                progress1 = new ProgressDialog(LoginActivity.this);
                progress1.setMessage("logging in :) ");
                // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress1.setIndeterminate(true);
                progress1.show();
                startActivityForResult(i, RC_SIGN_IN);
            }
        });
//        signoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Auth.GoogleSignInApi.signOut(gclient).setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        // displaymsg.setText("Signed Out");
//                    }
//                });
//                FirebaseUser user = mAuth.getCurrentUser();
//                if (user != null)
//                    Log.d("userid", user.getEmail());
//                FirebaseAuth.getInstance().signOut();
//                // displaymsg.setText("Signed Out");
//            }
//        });


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginfb = (LoginButton) findViewById(R.id.login_fb);
        loginfb.setReadPermissions("email", "public_profile");
        loginfb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook:onSuccess:", loginResult.toString());

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //  Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                // updateUI(null);
                // [END_EXCLUDE]
            }


            @Override
            public void onError(FacebookException error) {
                //Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                // updateUI(null);
                // [END_EXCLUDE]
            }
        });
//        loginfb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
//            }
//        });


        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {

                    Log.d("signeduser", firebaseAuth.getCurrentUser().getEmail());
                    if (login == 1) {

                        //  Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());


                        String uid=firebaseAuth.getCurrentUser().getUid();
//                        loginref1=FirebaseDatabase.getInstance().getReference()
//                                .child("users").child(firebaseAuth.getCurrentUser().getUid());
//                       loginlist2=loginref1.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                HashMap<String,String> uid_exists= (HashMap<String, String>) dataSnapshot.getValue();
//
//                                if(uid_exists!=null)
//                                {
//                                    fname1=uid_exists.get("DisplayName").toString();
//                                    if(uid_exists.get("ProfilePic")!=null)
//                                    photo1=uid_exists.get("ProfilePic");
//                                }
//                                else{
//                                    fname1 = firebaseAuth.getCurrentUser().getDisplayName().toString();
//                                    if(firebaseAuth.getCurrentUser().getPhotoUrl()!=null) {
//                                        photo1 = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
//                                    }
//                                }
//
//
//
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });



                        fname1 = firebaseAuth.getCurrentUser().getDisplayName().toString();
                        if(firebaseAuth.getCurrentUser().getPhotoUrl()!=null) {
                            photo1 = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
                        }



                  //      Log.d("fn1",fname1);
                       // Log.d("photo",photo1);

                      //  Log.d("profilefb", photo.toString());
                        //  mAuth.getCurrentUser()
                        FirebaseDatabase.getInstance().getReference()
                                .child("users").child(firebaseAuth.getCurrentUser().getUid()).child("DisplayName").setValue(fname1);
                          if(photo1!=null)
                        FirebaseDatabase.getInstance().getReference()
                                .child("users").child(firebaseAuth.getCurrentUser().getUid()).child("ProfilePic").setValue(photo1.toString());
                        FirebaseDatabase.getInstance().getReference()
                                .child("users").child(firebaseAuth.getCurrentUser().getUid()).child("EmailId").setValue(firebaseAuth.getCurrentUser().getEmail().toString());
                        OwnProfile profile = new OwnProfile();

                        profile.setDisplayName(fname1);
                        profile.setDisplayPic(photo1);
                        profile.setUid(firebaseAuth.getCurrentUser().getUid());
                        Intent i = new Intent(LoginActivity.this, GetConnected.class);
                        i.putExtra("ownprofile", profile);
                        startActivity(i);


                        //  Toast.makeText(getApplicationContext(),"singed successfully",Toast.LENGTH_SHORT);


                    }
                    else {
                        loginref = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(firebaseAuth.getCurrentUser().getUid());
                        loginlist = loginref.
                                addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap<String, String> userkeys = (HashMap<String, String>) dataSnapshot.getValue();
                                        if(userkeys!=null) {
                                            fname = userkeys.get("DisplayName");
                                            if (userkeys.containsKey("ProfilePic")) {
                                                photo = userkeys.get("ProfilePic");
                                            }
                                            OwnProfile profile = new OwnProfile();

                                            profile.setDisplayName(fname);
                                            if (photo != null)
                                                profile.setDisplayPic(photo);
                                            profile.setUid(firebaseAuth.getCurrentUser().getUid());
                                            Intent i = new Intent(LoginActivity.this, GetConnected.class);
                                            i.putExtra("ownprofile", profile);
                                            startActivity(i);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                }
            }
        };
        findViewById(R.id.btnlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        //  Log.d(TAG, "handleFacebookAccessToken:" + token);
        login = 1;
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progress3.dismiss();
                        if (task.isSuccessful()) {
                            login = 1;
                            Log.d("signIncredonComplete:", "success");
//                        OwnProfile profile = new OwnProfile();
//                        if (mAuth != null) {
//                            String fname = mAuth.getCurrentUser().getDisplayName().toString();
//                            String photo = mAuth.getCurrentUser().getPhotoUrl().toString();
//                            profile.setDisplayName(fname);
//                            profile.setDisplayPic(photo);
//                            profile.setUid(mAuth.getCurrentUser().getUid());
//                            Log.d("profilefb", photo.toString());
//                            //  mAuth.getCurrentUser()
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("users").child(mAuth.getCurrentUser().getUid()).child("DisplayName").setValue(fname);
//
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("users").child(mAuth.getCurrentUser().getUid()).child("ProfilePic").setValue(photo.toString());
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("users").child(mAuth.getCurrentUser().getUid()).child("EmailId").setValue(mAuth.getCurrentUser().getEmail().toString());
//
//                            //  Toast.makeText(getApplicationContext(),"singed successfully",Toast.LENGTH_SHORT);
//                            Intent i = new Intent(LoginActivity.this, GetConnected.class);
//                            i.putExtra("ownprofile", profile);
//                            startActivity(i);

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.d("signInWithCredential", "failure");

                            }
                        }
                    }
                    // ...

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(LoginActivity.this, "Email id already exists",
                        Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();

            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        login = 1;
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress1.dismiss();
                        login = 1;
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//                        String fname=mAuth.getCurrentUser().getDisplayName().toString();
//                        String photo=mAuth.getCurrentUser().getPhotoUrl().toString();
//                        Log.d("profilefb",photo.toString());
//                        //  mAuth.getCurrentUser()
//                         FirebaseDatabase.getInstance().getReference()
//                                .child("users").child(mAuth.getCurrentUser().getUid()).child("DisplayName").setValue(fname);
//
//                       FirebaseDatabase.getInstance().getReference()
//                                .child("users").child(mAuth.getCurrentUser().getUid()).child("ProfilePic").setValue(photo.toString());
//                        FirebaseDatabase.getInstance().getReference()
//                                .child("users").child(mAuth.getCurrentUser().getUid()).child("EmailId").setValue(mAuth.getCurrentUser().getEmail().toString());
//                        OwnProfile profile=new OwnProfile();
//
//                        profile.setDisplayName(fname);
//                        profile.setDisplayPic(photo);
//                        profile.setUid(mAuth.getCurrentUser().getUid());
//                        Intent i=new Intent(LoginActivity.this,GetConnected.class);
//                        i.putExtra("ownprofile",profile);
//                        startActivity(i);


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //if(result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            //  displaymsg.setText("Hello " + acct.getDisplayName());
            // ImageView img= (ImageView) findViewById(R.id.imageView);
            // Picasso.with(this).load(acct.getPhotoUrl()).into(img);


            // }
//            else
//            { displaymsg.setText("Couldn't Sign you in");
//            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mAuth.removeAuthStateListener(mListener);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "On Failed Connection " + connectionResult);
    }


    public void signIn() {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Missing Entries", Toast.LENGTH_LONG).show();
        } else {
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("logging in :) ");
            // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.show();


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_LONG).show();
                    } else if (task.isSuccessful()) {
                        //  login=1;
//                        OwnProfile profile=new OwnProfile();
//                        FirebaseUser user = task.getResult().getUser();
//
//                        profile.setDisplayName(user.getDisplayName());
//                        if(user.getPhotoUrl()!=null)
//                        profile.setDisplayPic(user.getPhotoUrl().toString());
//                        profile.setUid(user.getUid());
//                        Intent i=new Intent(LoginActivity.this,GetConnected.class);
//                        i.putExtra("ownprofile",profile);
//                        startActivity(i);
//                        progress.dismiss();


//                        Intent i=new Intent(LoginActivity.this,GetConnected.class);
//
//                       // User user=new User();
//                       // user.setEmail(email);
//                        //user.setPassword(password);
//                        i.putExtra("email",email);
//                        startActivity(i);
                    }
                }
            });
        }
    }

}