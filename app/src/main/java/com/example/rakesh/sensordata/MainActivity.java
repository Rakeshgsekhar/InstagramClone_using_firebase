package com.example.rakesh.sensordata;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button login,signup;
    EditText email,pass;
    String emailid,password;
    ProgressBar login_progress;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Initializing my view attributes
        login = (Button)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.register);
        email= (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        login_progress = (ProgressBar)findViewById(R.id.login_progress);
        //completed

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


                Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


            }else{
                Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
        }



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                emailid = email.getText().toString();
                password = pass.getText().toString();

                if(TextUtils.isEmpty(emailid) && TextUtils.isEmpty(password)){
                    login_progress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                sendtoMain();
                            }else {
                                Toast.makeText(MainActivity.this,""+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }

                            login_progress.setVisibility(View.INVISIBLE);
                        }
                    });

                }

            }
        });




    }

    public void sendtoMain(){
        Intent intent = new Intent(MainActivity.this,Home.class);
        startActivity(intent);

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){

            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();
        }
    }
}
