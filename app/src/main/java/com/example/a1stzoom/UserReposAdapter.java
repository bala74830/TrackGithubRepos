package com.example.a1stzoom;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserReposAdapter extends RecyclerView.Adapter<UserReposAdapter.UserReposViewHolder>{

    private ArrayList<Usermodel> repos;
    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String oname="";

    public UserReposAdapter(ArrayList<Usermodel> repos, Context context) {
        this.setRepos(repos);
        this.setContext(context);
    }

    public ArrayList<Usermodel> getRepos() {return repos;}

    public void setRepos(ArrayList<Usermodel> repos) {this.repos = repos;}

    public Context getContext() {return context;}

    public void setContext(Context context) {this.context = context;}



    @NonNull
    @Override
    public UserReposAdapter.UserReposViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_repo, parent, false);
        return new UserReposAdapter.UserReposViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReposAdapter.UserReposViewHolder holder, int position) {
        settings = holder.itemView.getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = settings.edit();
        oname=settings.getString("Ownername","");
        for (int i=0;i<position;i++)
        {
            Usermodel u=repos.get(i);
            //Usermodel u = repos.get(position);
            holder.repoName.setText(u.getName());
            if (u.getDescription()!=null){
                holder.repoDescription.setText(u.getDescription());
            }
            else {
                holder.repoDescription.setText("No Description found");
            }
        }
        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Usermodel usermodel=repos.get(position-1);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    String shareMessage= "\nCheck this Repo\n\n";
                    shareMessage = shareMessage + "https://github.com/"+ oname+"/"+usermodel.getName() +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    Intent chooserIntent = Intent.createChooser(shareIntent, "Open With");
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(chooserIntent);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.repoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usermodel usermodel=repos.get(position-1);
                Uri uri = Uri.parse("https://github.com/"+oname+"/"+usermodel.getName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public class UserReposViewHolder extends RecyclerView.ViewHolder{
        TextView repoName;
        TextView repoDescription,Ownername;
        ImageView sharebtn;
        LinearLayout repoll;

        public UserReposViewHolder(View v) {
            super(v);
            repoName = (TextView) v.findViewById(R.id.repoName);
            repoDescription = (TextView) v.findViewById(R.id.repoDescription);
            sharebtn = (ImageView) v.findViewById(R.id.sharbtn);
            repoll =(LinearLayout) v.findViewById(R.id.repoll);
            Ownername =(TextView) v.findViewById(R.id.OwName);
        }
    }
}
