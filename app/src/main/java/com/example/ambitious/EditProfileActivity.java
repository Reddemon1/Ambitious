package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView profileImage;
    private FirebaseUser user;
    private EditText profileName,profileBio,profileTelp,profileBirth,profileEmail;
    private ImageButton profileBack;
    private Button profileEdit;
    private String status;
    private String userid;
    private Uri imageUri;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tbuser");
    private DatabaseReference database;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private void showDateDialogBirth(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                profileBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        profileImage = (ImageView)findViewById(R.id.profileImage);
        profileImage.setOnClickListener(this);

        profileEdit = (Button) findViewById(R.id.profileEdit);
        profileEdit.setOnClickListener(this);

        profileBirth = (EditText)findViewById(R.id.profileBirth);
        profileBirth.setOnClickListener(this);

        profileBack = (ImageButton) findViewById(R.id.profileBack);
        profileBack.setOnClickListener(this);

        profileName = (EditText)findViewById(R.id.profileName);
        profileBio = (EditText)findViewById(R.id.profileBio);
        profileTelp = (EditText)findViewById(R.id.profileTelp);
        profileEmail = (EditText)findViewById(R.id.profileEmail);

        database = FirebaseDatabase.getInstance().getReference("tbuser").child(userid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser user = snapshot.getValue(tbuser.class);
                if (user != null){
                    profileName.setText(user.Name);
                    profileEmail.setText(user.Email);
                    if(snapshot.child("Bio").exists()){
                        profileBio.setText(user.Bio);
                    }
                    if(snapshot.child("Birth").exists()){
                        profileBirth.setText(user.Birth);
                    }
                    if(snapshot.child("Image").exists()){
                        Picasso.with(getApplicationContext()).load(user.Image).into(profileImage);
                    }
                    if(snapshot.child("Telp").exists()){
                        profileTelp.setText(user.Telp);
                    }
                    status = user.Status;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
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
            profileImage.setImageURI(imageUri);
        }
    }
    private void editProfile() {
        String ProfileName = profileName.getText().toString().trim();
        String ProfileBio = profileBio.getText().toString().trim();
        String ProfileBirth = profileBirth.getText().toString().trim();
        String ProfileTelp = profileTelp.getText().toString().trim();
        String ProfileEmail = profileEmail.getText().toString().trim();

        if(imageUri == null){
            Toast.makeText(getApplicationContext(),"Please Enter Your Contest Photo",Toast.LENGTH_LONG).show();
            profileImage.requestFocus();
            return;
        }
        if(ProfileName.isEmpty()){
            profileName.setError("This Field Cannot be Blank");
            profileName.requestFocus();
            return;
        }
        if(ProfileBio.isEmpty()){
            profileBio.setError("This Field Cannot be Blank");
            profileBio.requestFocus();
            return;
        }
        if(ProfileBirth.isEmpty()){
            profileBirth.setError("This Field Cannot be Blank");
            profileBirth.requestFocus();
            return;
        }
        if(ProfileTelp.isEmpty()){
            profileTelp.setError("This Field Cannot be Blank");
            profileTelp.requestFocus();
            return;
        }
        if(ProfileEmail.isEmpty()){
            profileEmail.setError("This Field Cannot be Blank");
            profileEmail.requestFocus();
            return;
        }
        uploadToFirebase(imageUri);

    }
    private void uploadToFirebase(Uri uri) {
        StorageReference fileref = storage.child(System.currentTimeMillis()+ "." + getFileExtension(uri));

        fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String Image = uri.toString();
                        String Name = profileName.getText().toString().trim();
                        String Bio = profileBio.getText().toString().trim();
                        String Birth = profileBirth.getText().toString().trim();
                        String Telp = profileTelp.getText().toString().trim();
                        String Email = profileEmail.getText().toString().trim();
                        String Status = status;

                        tbuser user = new tbuser( Name ,Email, Bio , Birth , Telp ,Image,Status );
                        ref.child(userid).setValue(user);
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profileEdit:
                editProfile();
                break;
            case R.id.profileImage:
                addProfileImage();
                break;
            case R.id.profileBirth:
                showDateDialogBirth();
                break;
            case R.id.profileBack:
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
        }
    }


}