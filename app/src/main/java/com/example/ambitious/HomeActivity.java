package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("tblomba");
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tbuser");;
    DatabaseReference req = FirebaseDatabase.getInstance().getReference("tbrequest");
    ArrayList<tblomba> list;
    MyAdapter MyAdapter;
    Date todayDate,releaseDate;
    private String userid;
    private String status,requestreturn;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.itemview);

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
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
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
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();

                        return true;
                }
                return false;
            }
        });
        CardView techCard = (CardView) findViewById(R.id.cardItem);
        techCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,TechActivity.class));
            }
        });
        CardView speechCard = (CardView) findViewById(R.id.cardView2);
        speechCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SpeechActivity.class));
            }
        });
        CardView scholarCard = (CardView) findViewById(R.id.cardView3);
        scholarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ScholarActivity.class));
            }
        });

        TextView name = findViewById(R.id.namaUser);
        ImageView userImage = findViewById(R.id.imageUser);
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser userprofile = snapshot.getValue(tbuser.class);

                if (userprofile != null){
                    String userName = userprofile.Name;
                    if(snapshot.child("Image").exists()){
                        Picasso.with(getApplicationContext()).load(userprofile.Image).into(userImage);
                    }
                    status = userprofile.Status;
                    name.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        list = new ArrayList<>();
        MyAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(MyAdapter);
        MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tblomba tblomba = list.get(position);
                Intent intent = new Intent(HomeActivity.this,ItemPageActivity.class);
                intent.putExtra("itemPageImage",tblomba.getImage());
                intent.putExtra("itemOpen",tblomba.getOpenDate());
                intent.putExtra("itemEnd",tblomba.getEndDate());
                intent.putExtra("itemNoParticipant",tblomba.getNoParticipant());
                intent.putExtra("itemName",tblomba.getName());
                intent.putExtra("itemUsername",tblomba.getUsername());
                intent.putExtra("itemDesc",tblomba.getDesc());
                intent.putExtra("contestId",tblomba.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                HomeActivity.this.startActivity(intent);
                finish();
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