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

public class ScholarActivity extends AppCompatActivity {
    private RecyclerView recyclerScholar;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("tblomba");
    ArrayList<tblomba> list;
    MyAdapter MyAdapter;
    Date todayDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar);
        ImageButton scholarback = (ImageButton) findViewById(R.id.scholarBack);
        scholarback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScholarActivity.this,HomeActivity.class));
                finish();
            }
        });

        recyclerScholar = (RecyclerView) findViewById(R.id.recyclerContest);
        recyclerScholar.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerScholar.setLayoutManager(gridLayoutManager);

        list = new ArrayList<>();
        MyAdapter = new MyAdapter(this,list);
        recyclerScholar.setAdapter(MyAdapter);
        MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tblomba tblomba = list.get(position);
                Intent intent = new Intent(ScholarActivity.this,ItemPageActivity.class);
                intent.putExtra("itemPageImage",tblomba.getImage());
                intent.putExtra("itemOpen",tblomba.getOpenDate());
                intent.putExtra("itemEnd",tblomba.getEndDate());
                intent.putExtra("itemNoParticipant",tblomba.getNoParticipant());
                intent.putExtra("itemName",tblomba.getName());
                intent.putExtra("itemUsername",tblomba.getUsername());
                intent.putExtra("itemDesc",tblomba.getDesc());
                intent.putExtra("contestId",tblomba.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ScholarActivity.this.startActivity(intent);
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
                            if(category.equals("ScholarShip")) {
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