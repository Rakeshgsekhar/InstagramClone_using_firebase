package com.example.rakesh.sensordata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AddNewPost extends AppCompatActivity {

    private Toolbar postnewtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        postnewtoolbar= findViewById(R.id.newpost_Toolbar);
        setSupportActionBar(postnewtoolbar);
        getSupportActionBar().setTitle("New POST");



    }
}
