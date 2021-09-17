package com.example.ambitious;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class EditContestActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView contestImage,s1,s2,s3,sContain;;
    private Button contestCreate;
    private String uid,id;
    private EditText contestEnd , contestOpen , contestName , contestDescription , contestRelease;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("tblomba");
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tbuser");
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private Spinner contestCategory;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String username;
    private String userid,imageCertificate;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Uri imageUri;
    private void showDateDialogOpen(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                contestOpen.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    private void showDateDialogRelease(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                contestRelease.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    private void showDateDialogEnd(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                contestEnd.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    private void addContestImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 2);
    }
    private void createContest() {
        String releaseDate = contestRelease.getText().toString().trim();
        String openDate = contestOpen.getText().toString().trim();
        String endDate = contestEnd.getText().toString().trim();
        String Name = contestName.getText().toString().trim();
        String contestDesc = contestDescription.getText().toString().trim();

        if(imageUri == null){
            Toast.makeText(EditContestActivity.this,"Please Enter Your Contest Photo",Toast.LENGTH_LONG).show();
            contestImage.requestFocus();
            return;
        }
        if(Name.isEmpty()){
            contestName.setError("This Field Cannot be Blank");
            contestName.requestFocus();
            return;
        }
        if(contestDesc.isEmpty()){
            contestDescription.setError("This Field Cannot be Blank");
            contestDescription.requestFocus();
            return;
        }
        if(releaseDate.isEmpty()){
            contestRelease.setError("This Field Cannot be Blank");
            contestRelease.requestFocus();
            return;
        }
        if(openDate.isEmpty()){
            contestOpen.setError("This Field Cannot be Blank");
            contestOpen.requestFocus();
            return;
        }

        if(endDate.isEmpty()){
            contestEnd.setError("This Field Cannot be Blank");
            contestEnd.requestFocus();
            return;
        }

        uploadToFirebase(imageUri);

    }

    private void uploadToFirebase(Uri uri) {
        StorageReference fileref = storage.child(System.currentTimeMillis()+ "." + getFileExtension(uri));
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tbuser userprofile = snapshot.getValue(tbuser.class);
                username = userprofile.Name;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           String image = uri.toString();
                           String name = contestName.getText().toString().trim();
                           String desc = contestDescription.getText().toString().trim();
                           String releaseDate= contestRelease.getText().toString().trim();
                           String openDate = contestOpen.getText().toString().trim();
                           String endDate = contestEnd.getText().toString().trim();
                           String category = contestCategory.getSelectedItem().toString();
                           database.child(id).child("image").setValue(image);
                           database.child(id).child("name").setValue(name);
                           database.child(id).child("desc").setValue(desc);
                           database.child(id).child("releaseDate").setValue(releaseDate);
                           database.child(id).child("openDate").setValue(openDate);
                           database.child(id).child("endDate").setValue(endDate);
                           database.child(id).child("category").setValue(category);
                           database.child(id).child("imageCertificate").setValue(imageCertificate);
                           startActivity(new Intent(getApplicationContext(),CreatedActivity.class));
                           overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                           finish();
                       }
                   });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditContestActivity.this,"Failed To Upload Photo",Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        contestEnd = (EditText) findViewById(R.id.contestEnd);
        contestEnd.setOnClickListener(this);

        contestRelease = (EditText) findViewById(R.id.contestRelease);
        contestRelease.setOnClickListener(this);

        contestName = (EditText) findViewById(R.id.contestName);

        contestCategory = (Spinner) findViewById(R.id.contestCategory);
        ArrayAdapter<String> Spinneradapter = new ArrayAdapter<String>(EditContestActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.category));
        Spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contestCategory.setAdapter(Spinneradapter);

        contestDescription = (EditText) findViewById(R.id.contestDescription);

        contestOpen = (EditText) findViewById(R.id.contestOpen);
        contestOpen.setOnClickListener(this);

        contestImage = (ImageView) findViewById((R.id.contestImage));
        contestImage.setOnClickListener(this);

        contestCreate = (Button) findViewById(R.id.contestCreate);
        contestCreate.setOnClickListener(this);

        sContain = (ImageView) findViewById(R.id.sContain);

        s1 = (ImageView) findViewById((R.id.s1));
        s1.setOnClickListener(this);
        s2 = (ImageView) findViewById((R.id.s2));
        s2.setOnClickListener(this);
        s3 = (ImageView) findViewById((R.id.s3));
        s3.setOnClickListener(this);

        ImageButton contestBack = (ImageButton) findViewById(R.id.contestBack);
        contestBack.setOnClickListener(this);
        id = getIntent().getStringExtra("Uid");
        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
        database.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tblomba fill = snapshot.getValue(tblomba.class);
                Picasso.with(getApplicationContext()).load(fill.image).into(contestImage);
                contestOpen.setText(fill.openDate);
                contestEnd.setText(fill.endDate);
                contestRelease.setText(fill.releaseDate);
                contestDescription.setText(fill.desc);
                contestName.setText(fill.name);
                imageCertificate = fill.imageCertificate;
                Picasso.with(getApplicationContext()).load(fill.imageCertificate).into(sContain);
                int spinnerPosition = Spinneradapter.getPosition(fill.category);
                contestCategory.setSelection(spinnerPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null ){

            imageUri = data.getData();
            contestImage.setImageURI(imageUri);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contestEnd:
                showDateDialogEnd();
                break;
            case R.id.contestOpen:
                showDateDialogOpen();
                break;
            case R.id.contestRelease:
                showDateDialogRelease();
                break;
            case R.id.contestImage:
                addContestImage();
                break;
            case R.id.contestCreate:
                createContest();
                break;
            case R.id.contestBack:
                contestback();
                break;
            case R.id.s1:
                imageCertificate = "https://firebasestorage.googleapis.com/v0/b/coba-5f5a3.appspot.com/o/s1.jpg?alt=media&token=2b71f858-c527-4bfb-a1b9-536ca137e68b";
                Picasso.with(getApplicationContext()).load(imageCertificate).into(sContain);
                break;
            case R.id.s2:
                imageCertificate = "https://firebasestorage.googleapis.com/v0/b/coba-5f5a3.appspot.com/o/s2.jpg?alt=media&token=a420cfb8-541e-430c-9cd1-2bed2e51749f";
                Picasso.with(getApplicationContext()).load(imageCertificate).into(sContain);
                break;
            case R.id.s3:
                imageCertificate = "https://firebasestorage.googleapis.com/v0/b/coba-5f5a3.appspot.com/o/s3.jpg?alt=media&token=d2393014-8e43-4a37-9a4f-7d1430662692";
                Picasso.with(getApplicationContext()).load(imageCertificate).into(sContain);
                break;
        }
    }

    private void contestback() {
        startActivity(new Intent(EditContestActivity.this,CreatedActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }


}