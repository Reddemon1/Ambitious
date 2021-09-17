 package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class VerifyActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView verifyWithKTP,verifyKTP;
    Button verifyRequest;
    private FirebaseUser user;
    private String uidrequest , uid , Email,Name;
    private DatabaseReference database;
    private DatabaseReference ref;
    private Uri imageUriKTP , imageUriwithKTP;
    private String photoWithKTP , photoKTP;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private void addKTPImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 2);
    }
    private void addWithKTPImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 3);
    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null ){
            imageUriKTP = data.getData();
            verifyKTP.setImageURI(imageUriKTP);
        }else if (requestCode == 3 && resultCode == RESULT_OK && data != null ){
            imageUriwithKTP = data.getData();
            verifyWithKTP.setImageURI(imageUriwithKTP);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        verifyKTP = (ImageView) findViewById(R.id.verifyKTP);
        verifyKTP.setOnClickListener(this);

        verifyWithKTP = (ImageView) findViewById(R.id.verifyWithKTP);
        verifyWithKTP.setOnClickListener(this);

        verifyRequest = (Button) findViewById(R.id.verifyRequest);
        verifyRequest.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_create);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_create:
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;
                    case R.id.nav_setting:
                        Toast.makeText(getApplicationContext(),"Setting Page",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verifyKTP:
                addKTPImage();
                break;
            case R.id.verifyWithKTP:
                addWithKTPImage();
                break;
            case R.id.verifyRequest:
                addrequest();
                break;
            case R.id.verifyBack:
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
        }
    }

    private void addrequest() {
        if(imageUriKTP == null){
            Toast.makeText(getApplicationContext(),"Please Enter Your Contest Photo",Toast.LENGTH_LONG).show();
            verifyKTP.requestFocus();
            return;
        }
        if(imageUriwithKTP == null){
            Toast.makeText(getApplicationContext(),"Please Enter Your Contest Photo",Toast.LENGTH_LONG).show();
            verifyWithKTP.requestFocus();
            return;
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference("tbuser");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser user = snapshot.getValue(tbuser.class);
                if (user != null){
                    Email = user.Email;
                    Name = user.Name;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database = FirebaseDatabase.getInstance().getReference("tbrequest");
        StorageReference filektp = storage.child(System.currentTimeMillis()+ "." + getFileExtension(imageUriKTP));
        StorageReference filewithktp = storage.child(System.currentTimeMillis()+ "." + getFileExtension(imageUriwithKTP));

        filewithktp.putFile(imageUriwithKTP).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filewithktp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        photoWithKTP = uri.toString();
                        database.child(uid).child("photoWithKTP").setValue(photoWithKTP);
                    }
                });
            }
        });
        filektp.putFile(imageUriKTP).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filektp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        photoKTP = uri.toString();
                        database.child(uid).child("photoKTP").setValue(photoKTP);
                        database.child(uid).child("Email").setValue(Email);
                        database.child(uid).child("uid").setValue(uid);
                        database.child(uid).child("Name").setValue(Name);
                        startActivity(new Intent(getApplicationContext(),ProgressActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                });
            }
        });
    }
}