package com.example.rakesh.sensordata;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpSettings extends AppCompatActivity {

    private Toolbar setuptoolbar;
    private CircleImageView profimage;
    private EditText userName;
    private String Uname;
    private Uri mainprofImage=null;
    private Button setup;
    private ProgressBar profilesettingbar;
    private String user_id;
    private Boolean changed = false;

    private StorageReference mStorageRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_settings);

        profimage = findViewById(R.id.profimage);
        userName = findViewById(R.id.userName);
        setup = findViewById(R.id.setup);
        profilesettingbar = findViewById(R.id.profilesettingbar);

        setuptoolbar = findViewById(R.id.setuptoolbar);
        setSupportActionBar(setuptoolbar);

        getSupportActionBar().setTitle("Settings");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        profilesettingbar.setVisibility(View.VISIBLE);
        setup.setEnabled(false);


        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult().exists()){


                        String result_name = task.getResult().getString("name");
                        String result_image = task.getResult().getString("image");

                        mainprofImage = Uri.parse(result_image);
                        userName.setText(result_name);

                        /**Can give Request Options here to provide defealt image.
                         *  ( incase our image from firestore doesnt load.)
                         *
                         *  RequestOptions placeholderrequest = new RequestOptions();
                         *  placeholderrequest.placeholder(R.drawable.defaultimage);**/


                        Glide.with(SetUpSettings.this).load(result_image).into(profimage);
                        /** Here is where i am tomorrow Start from here **/
                        Toast.makeText(SetUpSettings.this,"Data Exist",Toast.LENGTH_SHORT).show();

                    }

                }else{


                    Toast.makeText(SetUpSettings.this,"Firestore RetriveError :"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
                profilesettingbar.setVisibility(View.INVISIBLE);
                setup.setEnabled(true);


            }
        });







        profimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(SetUpSettings.this,Intent.Ga)

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SetUpSettings.this);
            }
        });


        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilesettingbar.setVisibility(View.VISIBLE);
               if(changed){
                   Uname = userName.getText().toString();

                   if(TextUtils.isEmpty(Uname) && mainprofImage!=null){


                       //mStorageRef = FirebaseStorage.getInstance().getReference();

                       user_id = firebaseAuth.getCurrentUser().getUid();

                       StorageReference image_path = mStorageRef.child("profile_images").child(user_id+".jpg");

                       image_path.putFile(mainprofImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                               if(task.isSuccessful()){

                                   StoreFireStore(task,Uname);

                               }else{
                                   profilesettingbar.setVisibility(View.INVISIBLE);
                                   Toast.makeText(SetUpSettings.this,"Error :"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                               }
                           }
                       });

                   }
               }else{
                   StoreFireStore(null,Uname);
               }
            }
        });


    }

    private void StoreFireStore(Task<UploadTask.TaskSnapshot> task,String UserName) {
        Uri download_uri;
        if(task != null){
            download_uri = task.getResult().getDownloadUrl();
        }else {
            download_uri = mainprofImage;
        }

        Toast.makeText(SetUpSettings.this,"Profile Image uploaded successfully",Toast.LENGTH_SHORT).show();
        Map<String,String> usermap = new HashMap<>();
        usermap.put("name",Uname);
        usermap.put("image",download_uri.toString());
        firebaseFirestore.collection("Users").document(user_id).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                profilesettingbar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){

                    Toast.makeText(SetUpSettings.this,"Successful",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SetUpSettings.this,Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SetUpSettings.this,"Firestore Error :"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainprofImage = result.getUri();
                profimage.setImageURI(mainprofImage);
                changed=true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
