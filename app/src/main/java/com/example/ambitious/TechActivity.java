package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TechActivity extends AppCompatActivity {
    private RecyclerView recyclerTech;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("tblomba");
    ArrayList<tblomba> list;
    MyAdapter MyAdapter;
    Date todayDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);

        ImageButton techback = (ImageButton) findViewById(R.id.techBack);
        techback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TechActivity.this,HomeActivity.class));
                finish();
            }
        });

        recyclerTech = (RecyclerView) findViewById(R.id.recyclerContest);
        recyclerTech.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerTech.setLayoutManager(gridLayoutManager);

        list = new ArrayList<>();
        MyAdapter = new MyAdapter(this,list);
        recyclerTech.setAdapter(MyAdapter);
        MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tblomba tblomba = list.get(position);
                Intent intent = new Intent(TechActivity.this,ItemPageActivity.class);
                intent.putExtra("itemPageImage",tblomba.getImage());
                intent.putExtra("itemOpen",tblomba.getOpenDate());
                intent.putExtra("itemEnd",tblomba.getEndDate());
//                intent.putExtra("itemNoParticipant",tblomba.getNoParticipant());
                intent.putExtra("itemName",tblomba.getName());
                intent.putExtra("itemUsername",tblomba.getUsername());
                intent.putExtra("itemDesc",tblomba.getDesc());
                intent.putExtra("contestId",tblomba.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TechActivity.this.startActivity(intent);
            }
        });
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String today = formatter.format(date);
        try {
            Date now = formatter.parse(today);
            todayDate = now;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    tblomba tblomba = dataSnapshot.getValue(tblomba.class);
                    String category = tblomba.category;
                    try {
                        Date release = formatter.parse(tblomba.releaseDate);
                        Date end = formatter.parse(tblomba.endDate);
                        if(todayDate.before(release) && todayDate.after(end) ) {

                        }else{
                            if(category.equals("Technology")) {
                                list.add(tblomba);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }
                MyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}