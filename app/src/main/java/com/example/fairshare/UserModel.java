package com.example.fairshare;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("username")
    String username;

    @SerializedName("number")
    String number;

    @SerializedName("password")
    String password;

    @SerializedName("age")
    int age;

    @SerializedName("gender")
    String gender;

    @SerializedName("email_id")
    String email;

    @SerializedName("updated_time")
    String updatedTime;

    public UserModel(String username, String number, String password, int age, String gender, String email, String updatedTime) {
        this.username = username;
        this.number = number;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.updatedTime = updatedTime;
    }
}

