package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinedActivity extends AppCompatActivity {
    private String id;
    private RecyclerView recyclerJoined;
    ArrayList<tbparticipant> list;
    joinedAdapter joinedAdapter;
    DatabaseReference database;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined);

        id = getIntent().getStringExtra("Uid");
        Button joinedEdit = (Button) findViewById(R.id.joinedEdit);
        joinedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditContestActivity.class);
                intent.putExtra("Uid",id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });



        ImageButton joinedBack = (ImageButton) findViewById(R.id.joinedBack);
        joinedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreatedActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        database = FirebaseDatabase.getInstance().getReference("tbparticipant").child(id);
        ref = FirebaseDatabase.getInstance().getReference("tblomba").child(id);
        Button joinedDelete = (Button) findViewById(R.id.joinedDelete);
        joinedDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.setValue(null);
                startActivity(new Intent(getApplicationContext(),CreatedActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
        recyclerJoined = (RecyclerView) findViewById(R.id.recyclerJoined);
        recyclerJoined.setHasFixedSize(true);
        recyclerJoined.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        list = new ArrayList<>();
        joinedAdapter = new joinedAdapter(this,list);
        recyclerJoined.setAdapter(joinedAdapter);
        joinedAdapter.setOnItemClickListener(new joinedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tbparticipant tbparticipant = list.get(position);
                Intent intent = new Intent(getApplicationContext(),JoinedActivity.class);
//                intent.putExtra("Uid",tblomba.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    tbparticipant tbparticipant = dataSnapshot.getValue(tbparticipant.class);
                    list.add(tbparticipant);
                }
                joinedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}