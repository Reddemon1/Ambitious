package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class SettingActivity extends AppCompatActivity {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tbuser");;
    DatabaseReference req = FirebaseDatabase.getInstance().getReference("tbrequest");
    private String status,requestreturn , userid;
    private FirebaseAuth mauth;
    private ImageButton settingBack;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingBack = (ImageButton) findViewById(R.id.settingBack);
        settingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        Button settingLogout = (Button) findViewById(R.id.settingLogout);
        settingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser userprofile = snapshot.getValue(tbuser.class);

                if (userprofile != null){
                    status = userprofile.Status;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        bottomNavigationView.setSelectedItemId(R.id.nav_setting);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_create:
                        if(status.equals("User")) {
                            if(requestreturn.equals("Done")) {
                                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                                startActivity(new Intent(getApplicationContext(),ProgressActivity.class));
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                finish();
                            }else{
                                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                                startActivity(new Intent(getApplicationContext(), VerifyActivity.class));
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                finish();
                            }
                        }else{
                            startActivity(new Intent(getApplicationContext(), CreatedActivity.class));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            finish();
                        }
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();
                        return true;
                    case R.id.nav_profile:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();

                        return true;
                    case R.id.nav_setting:
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
    }
}