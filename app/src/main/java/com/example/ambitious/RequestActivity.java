package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class RequestActivity extends AppCompatActivity {
    private RecyclerView recyclerReq;
    ArrayList<tbrequest> list;
    reqAdapter reqAdapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("tbrequest");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        ImageButton reqback = (ImageButton) findViewById(R.id.reqBack);
        reqback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeAdminActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        recyclerReq = (RecyclerView) findViewById(R.id.recyclerReq);
        recyclerReq.setHasFixedSize(true);
        recyclerReq.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        list = new ArrayList<>();
        reqAdapter = new reqAdapter(this,list);
        recyclerReq.setAdapter(reqAdapter);
        reqAdapter.setOnItemClickListener(new reqAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tbrequest tbrequest = list.get(position);
                Intent intent = new Intent(getApplicationContext(),VerifyAdmin.class);
                intent.putExtra("photoKTP",tbrequest.getPhotoKTP());
                intent.putExtra("photoWithKTP",tbrequest.getPhotoWithKTP());
                intent.putExtra("Name",tbrequest.getName());
                intent.putExtra("Email",tbrequest.getEmail());
                intent.putExtra("Uid",tbrequest.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    tbrequest tbrequest = dataSnapshot.getValue(tbrequest.class);
                    list.add(tbrequest);
                }
                reqAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}