package com.example.a1stzoom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addrepo,userrepo;
    ProgressBar pgbar;
    RecyclerView repositoriesrv;
    TextView norepos;
    List<GitHubRepo> reposlist = new ArrayList<>();
    ReposAdapter reposAdapter;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    PopupWindow popupWindow;
    boolean firstTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        settings = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = settings.edit();
        addrepo=findViewById(R.id.add_fab);
        userrepo=findViewById(R.id.add_fab2);
        pgbar=findViewById(R.id.progressbar);
        repositoriesrv=findViewById(R.id.repos_rv);
        norepos=findViewById(R.id.no_repos_text);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }
        Toast.makeText(this, "Please make sure u are connected with the Internet", Toast.LENGTH_LONG).show();
        firstTime=settings.getBoolean("ft",false);
        if (!firstTime){
            repositoriesrv.setVisibility(View.GONE);
            norepos.setVisibility(View.VISIBLE);
        }
            Gson gson = new Gson();
            String json = settings.getString("repos", null);
            Type type = new TypeToken<ArrayList<GitHubRepo>>() {}.getType();
            reposlist = gson.fromJson(json, type);
        if (reposlist == null) {
            reposlist = new ArrayList<>();
        }
        addrepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });
        repositoriesrv.setLayoutManager(new LinearLayoutManager(this));
        reposAdapter = new ReposAdapter(reposlist, getApplicationContext());
        repositoriesrv.setAdapter(reposAdapter);
        userrepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(i);
            }
        });
    }
    public void onButtonShowPopupWindowClick(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupwindow, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        EditText OwnerET,repositorynameET;
        Button okbtn;
        OwnerET=(EditText)popupView.findViewById(R.id.Owner);
        repositorynameET=(EditText)popupView.findViewById(R.id.Reponame);
        okbtn=(Button)popupView.findViewById(R.id.ok_btn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgbar.setVisibility(View.VISIBLE);
                editor.putString("Ownername",String.valueOf(OwnerET.getText()));
                editor.apply();
                loaddata(String.valueOf(OwnerET.getText()),String.valueOf(repositorynameET.getText()));
                pgbar.setVisibility(View.GONE);
            }
        });
    }
    public void loaddata(String Owner,String Reponame){
        final GitHubRepoEndPoint apiService =
                APIClient.getClient().create(GitHubRepoEndPoint.class);
        Call<GitHubRepo> call =apiService.getRepo(Owner, Reponame);
        call.enqueue(new Callback<GitHubRepo>() {
            @Override
            public void onResponse(Call<GitHubRepo> call, Response<GitHubRepo> response) {
                if (response.body()!=null){
                    reposlist.addAll(Collections.singleton(response.body()));
                    reposAdapter.notifyDataSetChanged();
                    norepos.setVisibility(View.GONE);
                    repositoriesrv.setVisibility(View.VISIBLE);
                    popupWindow.dismiss();
                    Gson gson = new Gson();
                    String json = gson.toJson(reposlist);
                    editor.putString("repos", json);
                    editor.apply();
                    firstTime=true;
                    editor.putBoolean("ft",firstTime);
                    editor.apply();
                }
                else {
                    Toast.makeText(MainActivity.this, "Repository Not Found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GitHubRepo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Repository Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}