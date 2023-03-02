package com.example.a1stzoom;

import com.google.gson.annotations.SerializedName;

public class GitHubRepo {

    private String name;
    private String description;

    public GitHubRepo(
            String description,
            String name) {

        this.setDescription(description);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
