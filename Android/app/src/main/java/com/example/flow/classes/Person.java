package com.example.flow.classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Person implements Serializable {
    @SerializedName("id")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("email")
    private String Email;
    @SerializedName("password")
    private String Password;
    @SerializedName("apiKey")
    private String ApiKey;

    public Person() {
    }

    public Person(String password, String email) {
        this.Email = email;
        this.Password = password;
    }

    public Person(String name, String password, String email) {
        this.Email = email;
        this.Password = password;
        this.Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getApiKey() {
        return ApiKey;
    }

    public void setApiKey(String apiKey) {
        ApiKey = apiKey;
    }
}