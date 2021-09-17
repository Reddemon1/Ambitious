package com.example.ambitious;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerTech;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("tblomba");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tbuser");;
    DatabaseReference req = FirebaseDatabase.getInstance().getReference("tbrequest");
    ArrayList<tblomba> list;
    private String userid;
    private String status,requestreturn;
    private FirebaseUser user;
    MyAdapter MyAdapter;
    Date todayDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_create:
                        if(status.equals("User")) {
                            if(requestreturn.equals("Done")) {
                                startActivity(new Intent(getApplicationContext(),ProgressActivity.class));
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                finish();
                            }else{
                                startActivity(new Intent(getApplicationContext(), VerifyActivity.class));
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                finish();
                            }
                        }else{
                            startActivity(new Intent(getApplicationContext(), CreatedActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();

                        return true;
                    case R.id.nav_setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    case R.id.nav_search:
                        return true;
                }
                return false;
            }
        });



        recyclerTech = (RecyclerView) findViewById(R.id.recyclerContest);
        recyclerTech.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerTech.setLayoutManager(gridLayoutManager);

        EditText Search = (EditText) findViewById(R.id.searchbar);
        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String today = formatter.format(date);
                try {
                    Date now = formatter.parse(today);
                    todayDate = now;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String valueSearch = Search.getText().toString().trim();
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        TextView searchtitle = (TextView) findViewById(R.id.searchtitle);
                        TextView teks = (TextView) findViewById(R.id.teks);
                        searchtitle.setText("Search result");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            tblomba tblomba = dataSnapshot.getValue(tblomba.class);
                            String name = tblomba.name;
                            try {
                                Date release = formatter.parse(tblomba.releaseDate);
                                Date end = formatter.parse(tblomba.endDate);
                                if(todayDate.before(release) && todayDate.after(end) ) {

                                }else{
                                    if (name.toLowerCase().contains(valueSearch) || name.contains(valueSearch)){

                                        list.add(tblomba);
                                        teks.setVisibility(View.GONE);
                                    }else if(valueSearch == null){
                                        searchtitle.setText("Recommend Contest");
                                        teks.setVisibility(View.GONE);
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
        });

        list = new ArrayList<>();
        MyAdapter = new MyAdapter(this,list);
        recyclerTech.setAdapter(MyAdapter);
        MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tblomba tblomba = list.get(position);
                Intent intent = new Intent(SearchActivity.this,ItemPageActivity.class);
                intent.putExtra("itemPageImage",tblomba.getImage());
                intent.putExtra("itemOpen",tblomba.getOpenDate());
                intent.putExtra("itemEnd",tblomba.getEndDate());
//                intent.putExtra("itemNoParticipant",tblomba.getNoParticipant());
                intent.putExtra("itemName",tblomba.getName());
                intent.putExtra("itemUsername",tblomba.getUsername());
                intent.putExtra("itemDesc",tblomba.getDesc());
                intent.putExtra("contestId",tblomba.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SearchActivity.this.startActivity(intent);
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
                            list.add(tblomba);
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