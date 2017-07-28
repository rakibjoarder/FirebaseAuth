package com.aust.rakib.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText emailET;
    private EditText passwordET;
    private FirebaseAuth firebaseAuth;
    private TextView signUpTV;
    private TextView signInTV;
    private Button signInBT;
    private Button signUpBT;
    LinearLayout signinLayout;
    LinearLayout signupLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(false);


        firebaseAuth= FirebaseAuth.getInstance();
        emailET= (EditText) findViewById(R.id.email_edittext);
        passwordET= (EditText) findViewById(R.id.password_edittext);
        signUpTV= (TextView) findViewById(R.id.signuptext);
        signInTV= (TextView) findViewById(R.id.signintext);
        signInBT= (Button) findViewById(R.id.signInbutton);
        signUpBT= (Button) findViewById(R.id.signUpbutton);
        signinLayout= (LinearLayout) findViewById(R.id.signinlayout);
        signupLayout= (LinearLayout) findViewById(R.id.signuplayout);
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInBT.setVisibility(View.GONE);
                signUpBT.setVisibility(View.VISIBLE);
                signupLayout.setVisibility(View.GONE);
                signinLayout.setVisibility(View.VISIBLE);
            }
        });
        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInBT.setVisibility(View.VISIBLE);
                signUpBT.setVisibility(View.GONE);
                signinLayout.setVisibility(View.GONE);
                signupLayout.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
     if(currentUser!=null)
     {
         startActivity(new Intent(MainActivity.this,Main2Activity.class));
     }

    }

      public void LogIn(View view) {
        if (!validateForm()) {
            return;
        }
        progressDialog.show();
         firebaseAuth.signInWithEmailAndPassword(emailET.getText().toString(),passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

               if(task.isSuccessful())
               {
                   Toast.makeText(MainActivity.this, "Signed In Succesfully", Toast.LENGTH_SHORT).show();
                   progressDialog.cancel();
                   startActivity(new Intent(MainActivity.this,Main2Activity.class));

               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });
    }

       public void SignUp(View view) {


        if (!validateForm()) {
            return;
        }
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emailET.getText().toString(),passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                    startActivity(new Intent(MainActivity.this,Main2Activity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }});
    }


    private boolean validateForm() {                             //Chec
        boolean valid = true;

        String email = emailET.getText().toString();
        if (TextUtils.isEmpty(email)) {
             emailET.setError("Required Email.");
            valid = false;
        } else {
            emailET.setError(null);
        }

        String password = passwordET.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Required password");
            valid = false;
        } else {
           passwordET.setError(null);
        }
        return valid;
    }
}
