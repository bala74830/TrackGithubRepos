package com.example.a1stzoom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubRepoEndPoint {
    @GET("repos/{user}/{repo}")
    Call<GitHubRepo> getRepo(@Path("user") String name,@Path("repo") String repo);

    @GET("users/{name}/repos")
    Call<ArrayList<Usermodel>> getuserRepo(@Path("name")String name);
}



