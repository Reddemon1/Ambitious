package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DaftarActivity extends AppCompatActivity {
    private DatabaseReference userdata,database,participant;
    private String uid,id,noparticipant,email,telp,name;
    private FirebaseUser user;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private EditText daftarName , daftarTelp , daftarEmail ,daftarAddress , daftarAccount;
    private TextView daftarNamaLomba;
    private ImageView daftarImage;

    private Uri imageUri;

    private void addProfileImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 2);
    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null ){
            imageUri = data.getData();
            daftarImage.setImageURI(imageUri);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);


        daftarName =(EditText) findViewById(R.id.daftarName);
        daftarTelp =(EditText) findViewById(R.id.daftarTelp);
        daftarEmail =(EditText) findViewById(R.id.daftarEmail);
        daftarAddress =(EditText) findViewById(R.id.daftarAddress);
        daftarAccount =(EditText) findViewById(R.id.daftarAccount);
        daftarImage = (ImageView) findViewById(R.id.daftarImage);
        ImageButton daftarBack = (ImageButton) findViewById(R.id.daftarBack);
        daftarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        daftarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileImage();
            }
        });

        daftarNamaLomba = (TextView) findViewById(R.id.daftarNamaLomba);
        id = getIntent().getStringExtra("contestId");
        daftarNamaLomba.setText(getIntent().getStringExtra("contestName"));
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        userdata = FirebaseDatabase.getInstance().getReference("tbuser").child(uid);
        database = FirebaseDatabase.getInstance().getReference("tblomba").child(id);
        participant = FirebaseDatabase.getInstance().getReference("tbparticipant").child(id);
        participant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int part = Math.toIntExact(snapshot.getChildrenCount());
                noparticipant = String.valueOf(part);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser userprofile = snapshot.getValue(tbuser.class);
                if (userprofile != null){
                    email = userprofile.Email;
                    daftarEmail.setText(email);
                    if(snapshot.child("Telp").exists()){
                        telp = userprofile.Telp;
                        daftarTelp.setText(telp);
                    }
                    if(snapshot.child("Telp").exists()){
                        name = userprofile.Name;
                        daftarName.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button daftarRegis = (Button) findViewById(R.id.daftarRegis);
        daftarRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = daftarName.getText().toString().trim();
                String Telp = daftarTelp.getText().toString().trim();
                String Email = daftarEmail.getText().toString().trim();
                String Address = daftarAddress.getText().toString().trim();
                String BankAccount = daftarAccount.getText().toString().trim();

                if(imageUri == null){
                    Toast.makeText(getApplicationContext(),"Please Enter Your ID Card or Student Card Photo",Toast.LENGTH_LONG).show();
                    daftarImage.requestFocus();
                    return;
                }
                if(Name.isEmpty()){
                    daftarName.setError("This Field Cannot be Blank");
                    daftarName.requestFocus();
                    return;
                }
                if(Telp.isEmpty()){
                    daftarTelp.setError("This Field Cannot be Blank");
                    daftarTelp.requestFocus();
                    return;
                }
                if(Email.isEmpty()){
                    daftarEmail.setError("This Field Cannot be Blank");
                    daftarEmail.requestFocus();
                    return;
                }
                if(Address.isEmpty()){
                    daftarAddress.setError("This Field Cannot be Blank");
                    daftarAddress.requestFocus();
                    return;
                }
                if(BankAccount.isEmpty()){
                    daftarAccount.setError("This Field Cannot be Blank");
                    daftarAccount.requestFocus();
                    return;
                }


                StorageReference fileref = storage.child(System.currentTimeMillis()+ "." + getFileExtension(imageUri));
                fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String name = daftarName.getText().toString().trim();
                                String telp = daftarTelp.getText().toString().trim();
                                String email = daftarEmail.getText().toString().trim();
                                String address = daftarAddress.getText().toString().trim();
                                String bankAccount = daftarAccount.getText().toString().trim();
                                String image = uri.toString();

                                int no = Integer.parseInt(noparticipant);
                                int hasil = no+1;
                                noparticipant = String.valueOf(hasil);
                                database.child("noParticipant").setValue(noparticipant);

                                tbparticipant newparticipant = new tbparticipant(name,telp,email,address,bankAccount,uid,image);
                                participant.child(uid).setValue(newparticipant);
                                Intent done = new Intent(getApplicationContext(),RegisteredActivity.class);
                                done.putExtra("contestName",getIntent().getStringExtra("contestName"));
                                done.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(done);
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),"Failed To Upload Photo",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }
}