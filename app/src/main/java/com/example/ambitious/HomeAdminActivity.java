package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private CardView cardUser,cardInstituion,cardContest;
    private DatabaseReference database,lomba,req,dbuser;
    private String uid,Name,countcontest,countreq,countuser;
    private TextView adminName,countContest,countReq,countUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        adminName = (TextView) findViewById(R.id.namaAdmin);
        countContest = (TextView) findViewById(R.id.countContest);
        countReq = (TextView) findViewById(R.id.countReq);
        countUser = (TextView) findViewById(R.id.countUser);

        Button adminSignOut = (Button) findViewById(R.id.adminSignOut);
        adminSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        database = FirebaseDatabase.getInstance().getReference("tbuser");
        database.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser user = snapshot.getValue(tbuser.class);
                Name = user.Name;
                adminName.setText(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lomba = FirebaseDatabase.getInstance().getReference("tblomba");
        lomba.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int contest = Math.toIntExact(snapshot.getChildrenCount());
                countcontest = String.valueOf(contest);
                countContest.setText(countcontest);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        req = FirebaseDatabase.getInstance().getReference("tbrequest");
        req.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int contest = Math.toIntExact(snapshot.getChildrenCount());
                countreq = String.valueOf(contest);
                countReq.setText(countreq);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbuser = FirebaseDatabase.getInstance().getReference("tbuser");
        dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int user = Math.toIntExact(snapshot.getChildrenCount());
                countuser = String.valueOf(user);
                countUser.setText(countuser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cardContest = (CardView) findViewById(R.id.cardContest);
        cardContest.setOnClickListener(this);

        cardInstituion = (CardView) findViewById(R.id.cardInstitution);
        cardInstituion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardContest:
                startActivity(new Intent(getApplicationContext(), ContestActivity.class));
                finish();
                break;
            case R.id.cardInstitution:
                startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                finish();
                break;
            case R.id.cardUser:
                break;
        }
    }
}