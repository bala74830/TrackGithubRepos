package com.example.a1stzoom;

public class Usermodel {
    private String name;
    private String description;
    private String[] model;

    public Usermodel(String name, String description, String[] model) {
        this.name = name;
        this.description = description;
        this.model = model;
    }

    public Usermodel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Usermodel() {
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

    public String[] getModel() {
        return model;
    }

    public void setModel(String[] model) {
        this.model = model;
    }
}
