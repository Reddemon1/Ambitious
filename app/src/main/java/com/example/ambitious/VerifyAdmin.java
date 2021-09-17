package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class VerifyAdmin extends AppCompatActivity implements View.OnClickListener {

    private TextView adminVerName,adminVerEmail;
    private ImageView adminVerKTP,adminVerWithKTP;
    private Button accept, decline;
    private DatabaseReference dbreq , dbuser;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_admin);
        uid = getIntent().getStringExtra("Uid");
        adminVerEmail = (TextView) findViewById(R.id.adminVerEmail);
        adminVerEmail.setText(getIntent().getStringExtra("Email"));

        adminVerName = (TextView) findViewById(R.id.adminVerName);
        adminVerName.setText(getIntent().getStringExtra("Name"));

        adminVerKTP = (ImageView)findViewById(R.id.adminVerKTP);
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("photoKTP")).into(adminVerKTP);

        adminVerWithKTP = (ImageView)findViewById(R.id.adminVerWithKTP);
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("photoWithKTP")).into(adminVerWithKTP);

        accept = (Button) findViewById(R.id.adminVerAccept);
        accept.setOnClickListener(this);

        decline = (Button) findViewById(R.id.adminVerDecline);
        decline.setOnClickListener(this);

        dbuser = FirebaseDatabase.getInstance().getReference("tbuser").child(uid).child("Status");
        dbreq = FirebaseDatabase.getInstance().getReference("tbrequest").child(uid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adminVerAccept:
                chooseaccept();
                break;
            case R.id.adminVerDecline:
                choosedecline();
                break;
        }
    }

    private void chooseaccept() {
        dbreq.setValue(null);
        dbuser.setValue("Institution").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Successfully Update "+getIntent().getStringExtra("Name")+" to be an Institution",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),RequestActivity.class));
                    finish();
                }
            }
        });
    }

    private void choosedecline() {
        dbreq.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Successfully Decline "+getIntent().getStringExtra("Name")+" to be an Institution",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),RequestActivity.class));
                    finish();
                }
            }
        });
    }
}