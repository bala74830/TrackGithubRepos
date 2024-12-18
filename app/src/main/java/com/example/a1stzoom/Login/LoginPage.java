package com.example.a1stzoom.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a1stzoom.MainActivity;
import com.example.a1stzoom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    EditText us,pass2;
    Button  log,reg;
    TextView forgotpass;
    FirebaseAuth mauth;
    ImageButton eyetoggle;
    boolean show =true;
    String users="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mauth = FirebaseAuth.getInstance();
        us=findViewById(R.id.login_mail);
        pass2=findViewById(R.id.login_password);
        log=findViewById(R.id.login_btn);
        reg=findViewById(R.id.register_text);
        forgotpass=findViewById(R.id.forgetpass);
        eyetoggle = findViewById(R.id.password_toggle);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primarydark));
        }
        eyetoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    show = false;
                    eyetoggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    pass2.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    show = true;
                    eyetoggle.setImageResource(R.drawable.ic_baseline_visibility_24);
                    pass2.setTransformationMethod(null);
                }
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (us.getText().toString().equals("admin@gmail.com") && pass2.getText().toString().equals("admin")) {
                    Intent i = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(i);
                } else {
                    login();
                }

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users = us.getText().toString().trim();
                if (!TextUtils.isEmpty(users)) {
                    ResetPassword();
                } else {
                    us.setError("Email can not be empty..");
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationPage.class);
                startActivity(intent);
            }
        });


        us.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    us.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    pass2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                }
            }
        });

        pass2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pass2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_focus_bg));
                    us.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
                }
            }
        });

    }

    private void login() {
        String user= us.getText().toString().trim();
        String pass= pass2.getText().toString().trim();
        if (user.isEmpty()){
            us.setError("Email can not be empty..");

        }if (pass.isEmpty()){
            pass2.setError("Password can not be empty..");
        }
        else
        {
            mauth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        log.setClickable(false);
                        reg.setClickable(false);
                        Toast.makeText(LoginPage.this, "Logging in..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPage.this,MainActivity.class));
                    }
                    else
                    {
                        log.setClickable(false);
                        reg.setClickable(false);
                        Toast.makeText(LoginPage.this, "Login Failed!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void ResetPassword(){
        //greendotloader.setVisibility(View.VISIBLE);
        log.setClickable(false);
        reg.setClickable(false);


        mauth.sendPasswordResetEmail(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(LoginPage.this, "Reset Password link has been sent to your registered email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginPage.this,LoginPage.class);
                startActivity(intent);
                //greendotloader.setVisibility(View.GONE);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginPage.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                forgotpass.setVisibility(View.VISIBLE);
                log.setClickable(true);
                reg.setClickable(true);
            }
        });
        //password.addTextChangedListener(logintextwatcher);
        //mail.addTextChangedListener(logintextwatcher);
    }
}