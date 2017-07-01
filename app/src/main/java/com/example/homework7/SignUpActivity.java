package com.example.homework7;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText fullname, lastname;
    EditText email;
    EditText password;
    Button signupbtn;
    Button cancelbtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference usertReference;
    DatabaseReference usertReference2;
    RadioButton rb;
    RadioGroup rg;
    String radiovalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Expense App(Sign Up)");
        fullname = (EditText) findViewById(R.id.editText);
        lastname = (EditText) findViewById(R.id.editText4);

        email = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText3);
        signupbtn = (Button) findViewById(R.id.btnsignup);
        cancelbtn = (Button) findViewById(R.id.btncancel);
        mAuth = FirebaseAuth.getInstance();
        rg= (RadioGroup) findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb= (RadioButton) findViewById(checkedId);
                radiovalue=String.valueOf(rb.getText());
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    FirebaseDatabase.getInstance().getReference()
                            .child("users").child(user.getUid()).child("DisplayName").setValue(fullname.getText().toString() + " " + lastname.getText().toString());
                    FirebaseDatabase.getInstance().getReference()
                            .child("users").child(user.getUid()).child("EmailId").setValue(email.getText().toString());
                    FirebaseDatabase.getInstance().getReference()
                            .child("users").child(user.getUid()).child("Gender").setValue(radiovalue);


                    Log.d("msg1", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("mgs2", "onAuthStateChanged:signed_out");
                }
            }
        };
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signUp() {
        final String email1 = email.getText().toString();
        final String password1 = password.getText().toString();
        if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(password1)) {
            Toast.makeText(SignUpActivity.this, "Missing Entries", Toast.LENGTH_LONG).show();
        } else if (password1.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Passwords should be more than 5  characters", Toast.LENGTH_LONG).show();
        } else {
            mAuth.fetchProvidersForEmail(email1).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    if (!task.isSuccessful()) {
                        ///////// getProviders() will return size 1. if email ID is available.
                        Toast.makeText(SignUpActivity.this, "Email id already exists, please select a different email id ",
                                Toast.LENGTH_SHORT).show();
                        // task.getResult().getProviders();
                        //return;
                        email.setError("email already exists");
                    }
                }
            });


            mAuth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Success", "createUserWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Email id already exists, please select a different email id",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "User has been created",
                                Toast.LENGTH_SHORT).show();
//                        User user=new User();
//                        user.setName(fullname.getText().toString());
//                        user.setEmail(email1);
//                        user.setPassword(password1);


                        //startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                        FirebaseAuth.getInstance().signOut();
                        finish();

                    }
                }
            });
        }
    }

    public void cancel() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        //finish();
    }


}





