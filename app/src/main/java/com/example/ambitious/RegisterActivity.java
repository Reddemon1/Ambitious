package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerUser;
    private TextView loginPage;
    private EditText TextName , TextEmail , TextPass;
    private CheckBox checkterm;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        registerUser = (Button) findViewById(R.id.button1);
        registerUser.setOnClickListener(this);

        loginPage = (TextView) findViewById(R.id.btnpagelogin);
        loginPage.setOnClickListener(this);

        checkterm = (CheckBox) findViewById(R.id.checkterm);

        TextName = (EditText) findViewById(R.id.testNama);
        TextEmail = (EditText) findViewById(R.id.testEmail);
        TextPass = (EditText) findViewById(R.id.testPass);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button1:
                registerUser();
                break;
            case R.id.btnpagelogin:
                startActivity( new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
                break;
        }
    }


    private void registerUser() {

        String Name = TextName.getText().toString().trim();
        String Email = TextEmail.getText().toString().trim();
        String Pass = TextPass.getText().toString().trim();
        String Status = "User";

        if (Name.isEmpty()) {
            TextName.setError("Username Cannot be Blank");
            TextName.requestFocus();
            return;
        }
        if (Email.isEmpty()) {
            TextEmail.setError("Email Cannot be Blank");
            TextEmail.requestFocus();
            return;
        }
        if (Pass.isEmpty()) {
            TextPass.setError("Password Cannot be Blank");
            TextPass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            TextEmail.setError("Use A Valid Email");
            TextEmail.requestFocus();
            return;
        }
        if (Pass.length() < 6){
            TextPass.setError("Password Cannot Less than 6 Characters");
            TextPass.requestFocus();
            return;
        }
        if (!checkterm.isChecked()){
            Toast.makeText(RegisterActivity.this,"You Must Accept The Term and Privacy Policy",Toast.LENGTH_LONG).show();
            checkterm.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(Email , Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   tbuser user = new  tbuser(Name , Email , Status );

                    FirebaseDatabase.getInstance().getReference("tbuser")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Your Account Has Been Registered", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        TextPass.setText("");
                                        TextName.setText("");
                                        TextEmail.setText("");
                                        mAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(RegisterActivity.this , "Failed To Create Account",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        return;
                                    }
                                }
                            });

                }else{
                    Toast.makeText(RegisterActivity.this , "Failed To Create Account",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
            }
        });
    }
}