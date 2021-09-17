package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private EditText profileName,profileBio,profileBirth,profileEmail,profileTelp;
    private ImageView profileImage;
    private FirebaseUser user;
    private DatabaseReference database;
    private Button profileEdit;
    private String uid , status , requestreturn , userid;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tbuser");;
    DatabaseReference req = FirebaseDatabase.getInstance().getReference("tbrequest");
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        req.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    requestreturn = "Done";
                }else{
                    requestreturn = "verify";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_create:
                        if(status.equals("User")) {
                            if (requestreturn.equals("Done")) {
                                startActivity(new Intent(getApplicationContext(), ProgressActivity.class));
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                finish();
                                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                            } else {
                                startActivity(new Intent(getApplicationContext(), VerifyActivity.class));
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                finish();
                                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                            }
                        }else{
                            startActivity(new Intent(getApplicationContext(), CreatedActivity.class));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            finish();
                            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        }
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        return true;
                    case R.id.nav_profile:
                        return true;
                    case R.id.nav_setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        return true;
                    case R.id.nav_search:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();
                        return true;
                }
                return false;
            }
        });

        profileName = (EditText) findViewById(R.id.profileName);
        profileBio = (EditText) findViewById((R.id.profileBio));
        profileBirth = (EditText) findViewById((R.id.profileBirth));
        profileEmail = (EditText) findViewById((R.id.profileEmail));
        profileTelp = (EditText) findViewById((R.id.profileTelp));
        profileImage = (ImageView) findViewById(R.id.profileImage);
        ImageButton profileBack = (ImageButton) findViewById(R.id.profileBack);
        profileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
        profileEdit = (Button) findViewById(R.id.profileEdit);
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance().getReference("tbuser").child(uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser user = snapshot.getValue(tbuser.class);
                if (user != null){
                    status = user.Status;
                    profileName.setText(user.Name);
                    profileEmail.setText(user.Email);
                    if(snapshot.child("Bio").exists()){
                        profileBio.setText(user.Bio);
                    }
                    if(snapshot.child("Image").exists()){
                        Picasso.with(getApplicationContext()).load(user.Image).into(profileImage);
                    }
                    if(snapshot.child("Birth").exists()){
                        profileBirth.setText(user.Birth);
                    }
                    if(snapshot.child("Telp").exists()){
                        profileTelp.setText(user.Telp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}