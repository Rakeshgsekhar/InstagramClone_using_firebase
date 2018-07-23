package com.example.rakesh.sensordata;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private Toolbar hometoolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton addpost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hometoolbar = (Toolbar)findViewById(R.id.home_toolbar);
        mAuth = FirebaseAuth.getInstance();
        addpost = findViewById(R.id.addPostBtn);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this,AddNewPost.class);
                startActivity(intent);
                finish();
            }
        });


        getSupportActionBar().setTitle("POPOYE");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.userlogout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);
                finish();
        }

        return true;
    }
}
