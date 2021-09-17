package com.example.ambitious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button  loginUser;
    private TextView registerPage;
    private TextView btnforgot;
    private ProgressBar progressBar;
    private ImageView btnShow , btnShow2;
    private FirebaseUser user;
    private String userid;
    private DatabaseReference reference;
    private EditText TextPass , TextEmail;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_login);
          mAuth = FirebaseAuth.getInstance();

          registerPage = (TextView) findViewById(R.id.registerPage);
          registerPage.setOnClickListener(this);

          btnforgot = (TextView) findViewById(R.id.btnforgot);
          btnforgot.setOnClickListener(this);

          progressBar = (ProgressBar) findViewById(R.id.progressLogin);

          loginUser = (Button) findViewById(R.id.btnlogin);
          loginUser.setOnClickListener(this);

          btnShow = (ImageView) findViewById(R.id.btnShow);
          btnShow.setOnClickListener(this);

          btnShow2 = (ImageView) findViewById(R.id.btnShow2);
          btnShow2.setOnClickListener(this);

          TextEmail = (EditText) findViewById(R.id.loginEmail);
          TextPass = (EditText) findViewById(R.id.loginPass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerPage:
                startActivity( new Intent(this,RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                break;
            case R.id.btnlogin:
                loginuser();
                break;
            case R.id.btnforgot:
                startActivity(new Intent(this,ForgetActivity.class));
                finish();
                break;
            case R.id.btnShow:
                TextPass.setInputType(InputType.TYPE_CLASS_TEXT);
                btnShow.setVisibility(View.GONE);
                btnShow2.setVisibility(View.VISIBLE);
                break;
            case R.id.btnShow2:
                TextPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnShow2.setVisibility(View.GONE);
                btnShow.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loginuser() {
        progressBar.setVisibility(View.VISIBLE);
        String Email = TextEmail.getText().toString().trim();
        String Pass = TextPass.getText().toString().trim();
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


        mAuth.signInWithEmailAndPassword(Email , Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("tbuser");
                    userid = user.getUid();
                    reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            tbuser userprofile = snapshot.getValue(tbuser.class);

                            if (userprofile != null) {
                                String Status = userprofile.Status;
                                if(Status.matches("Admin")){
                                    startActivity(new Intent(LoginActivity.this,HomeAdminActivity.class));
                                    finish();
                                }else{
                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//

                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Failed To Login Please Check your Email Or Password",Toast.LENGTH_LONG).show();
                    TextEmail.setText("");
                    TextPass.setText("");
                    return;
                }
            }
        });
    }
}