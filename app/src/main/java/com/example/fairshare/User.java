package com.example.fairshare;

public class User {
    String username;
    String phone;
    String email;
    int age;

    public User(String username, String phone, String email, int age) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.age = age;
    }

    public String getUsername() { return username; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
}
