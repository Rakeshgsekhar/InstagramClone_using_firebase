package com.example.rakesh.sensordata;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText regemail,regpass,regconpass;
    Button register;
    String email,pass,conpas;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regemail = (EditText)findViewById(R.id.regemail);
        regpass = (EditText)findViewById(R.id.regpass);
        regconpass = (EditText)findViewById(R.id.regconfpass);
        register = (Button)findViewById(R.id.submit);


        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = regemail.getText().toString();
                pass = regpass.getText().toString();
                conpas = regconpass.getText().toString();

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) && TextUtils.isEmpty(conpas)){
                    if(pass.equals(conpas)){

                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,task.getResult().toString(),Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(RegisterActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this,"Password doesn't Match",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
