package com.example.a1stzoom;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.startActivity;
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

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ReposViewHolder>  {

    private List<GitHubRepo> repos;
    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String oname="";

    public ReposAdapter(List<GitHubRepo> repos, Context context) {
        this.setRepos(repos);
        this.setContext(context);
    }

    public List<GitHubRepo> getRepos() {return repos;}

    public void setRepos(List<GitHubRepo> repos) {this.repos = repos;}

    public Context getContext() {return context;}

    public void setContext(Context context) {this.context = context;}

    public static class ReposViewHolder extends RecyclerView.ViewHolder {
        TextView repoName;
        TextView repoDescription,Ownername;
        ImageView sharebtn;
        LinearLayout repoll;
//        SharedPreferences settings;
//        SharedPreferences.Editor editor;
        public ReposViewHolder(View v) {
            super(v);
            repoName = (TextView) v.findViewById(R.id.repoName);
            repoDescription = (TextView) v.findViewById(R.id.repoDescription);
            sharebtn = (ImageView) v.findViewById(R.id.sharbtn);
            repoll =(LinearLayout) v.findViewById(R.id.repoll);
            Ownername =(TextView) v.findViewById(R.id.OwName);
//            settings = v.getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
//            editor = settings.edit();
        }
    }

    @Override
    public ReposViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_repo, parent, false);
        return new ReposViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ReposViewHolder holder, final int position) {
        holder.repoName.setText(repos.get(position).getName());
        if (repos.get(position).getDescription()!=null){
            holder.repoDescription.setText(repos.get(position).getDescription());
        }
        else {
         holder.repoDescription.setText("No Description found");
        }
        settings = holder.itemView.getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = settings.edit();
        //oname= settings.getString("Ownername","");
        holder.Ownername.setText(settings.getString("Ownername",""));
//        String oname=holder.settings.getString("Ownername","");
        oname=holder.Ownername.getText().toString();
        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    String shareMessage= "\nCheck this Repo\n\n";
                    shareMessage = shareMessage + "https://github.com/"+ oname+"/"+repos.get(position).getName() +"\n\n";
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
            public void onClick(View view) {
//                https://github.com/bala74830/GuesstheWord

                Uri uri = Uri.parse("https://github.com/"+oname+"/"+repos.get(position).getName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return repos.size();}
}

