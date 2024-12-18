package com.example.a1stzoom;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
    EditText usertxt;
    RecyclerView recyclerView;
    FloatingActionButton fbtn;
    String uname="";
    ArrayList<Usermodel> reposlist = new ArrayList<>();
    UserReposAdapter userReposAdapter;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_repo);
        usertxt=findViewById(R.id.textuser);
        recyclerView=findViewById(R.id.userrv);
        fbtn=findViewById(R.id.gobtn);
        getSupportActionBar().hide();
        settings = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = settings.edit();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usertxt.getText().toString().equals(" "))
                {
                    Toast.makeText(MainActivity2.this, "Please enter username", Toast.LENGTH_SHORT).show();
                }
                uname=usertxt.getText().toString();
                editor.putString("Ownername",uname);
                editor.apply();
                 GitHubRepoEndPoint apiService =
                        APIClient.getClient().create(GitHubRepoEndPoint.class);
                Call<ArrayList<Usermodel>> call =apiService.getuserRepo(uname);
                call.enqueue(new Callback<ArrayList<Usermodel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Usermodel>> call, Response<ArrayList<Usermodel>> response) {
                        if (response.body()!=null){
                            reposlist = response.body();
                            userReposAdapter = new UserReposAdapter(reposlist, getApplicationContext());
                            recyclerView.setAdapter(userReposAdapter);
                            userReposAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(MainActivity2.this, "Repo not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Usermodel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(MainActivity2.this, "api call failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}