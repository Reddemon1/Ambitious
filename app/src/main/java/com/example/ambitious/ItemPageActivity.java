package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ItemPageActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference ref;
    private Date todayDate,opendate;
    private String uid,contestId,open,openDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);

        ImageView itemPageImage = (ImageView) findViewById(R.id.itemPageImage);
        TextView itemName = (TextView) findViewById(R.id.itemName);
        TextView itemOpen = (TextView) findViewById(R.id.itemOpen);
        TextView itemEnd = (TextView) findViewById(R.id.itemEnd);
        TextView itemDesc = (TextView) findViewById(R.id.itemDesc);
        TextView itemNoParticipant = (TextView) findViewById(R.id.itemNoParticipant);
        TextView itemUsername = (TextView) findViewById(R.id.itemUsername);
        contestId =  getIntent().getStringExtra("contestId");
        ref = FirebaseDatabase.getInstance().getReference("tblomba").child(contestId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tblomba tblomba = snapshot.getValue(tblomba.class);
                open = tblomba.openDate;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ImageButton itemBack = (ImageButton) findViewById(R.id.itemBack);
        itemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });


        String gambar = getIntent().getStringExtra("itemPageImage") ;
        Picasso.with(this).load(gambar).into(itemPageImage);

        itemName.setText(getIntent().getStringExtra("itemName"));
        itemOpen.setText(getIntent().getStringExtra("itemOpen"));
        openDate = getIntent().getStringExtra("itemOpen");
        itemEnd.setText(getIntent().getStringExtra("itemEnd"));
        itemDesc.setText(getIntent().getStringExtra("itemDesc"));
        itemNoParticipant.setText(getIntent().getStringExtra("itemNoParticipant"));
        itemUsername.setText(getIntent().getStringExtra("itemUsername"));

        Button itemRegis = (Button) findViewById(R.id.contestRegis);
        itemRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String today = formatter.format(date);
                try {
                    Date now = formatter.parse(today);
                    todayDate = now;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    opendate  = formatter.parse(openDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(todayDate.before(opendate)){
                    Toast.makeText(getApplicationContext(),"The Registration will be Open at "+openDate,Toast.LENGTH_LONG).show();
                }else {


                    user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = user.getUid();
                    DatabaseReference participant = FirebaseDatabase.getInstance().getReference("tbparticipant").child(contestId);
                    DatabaseReference db = participant.child(uid);
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean ss = snapshot.exists();
                            if (ss) {
                                Toast.makeText(getApplicationContext(), "You Have Registered To This Contest", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), DaftarActivity.class);
                                intent.putExtra("contestId", contestId);
                                intent.putExtra("contestName", getIntent().getStringExtra("itemName"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}