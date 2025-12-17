package com.example.fairshare;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseRepository {

    private final SupabaseAPI api;
    private final Context context;

    public SupabaseRepository(Context context) {
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mtsltrkoinjqymqialza.supabase.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(SupabaseAPI.class);
    }

    public void registerUser(String username, String number, String password,
                             int age, String gender, String email) {

        if (username.isEmpty() || number.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (number.length() != 10) {
            Toast.makeText(context, "Invalid Number", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
        ).format(new Date());

        UserModel user = new UserModel(username, number, password, age, gender, email, currentTime);

        api.registerUser(user).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Username or number already exists", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("SUPABASE_ERROR", "Network failed", t);
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //Login
    public void loginUser(String username,
                          String password,
                          Callbacks.LoginCallback callback) {

        api.loginUser(
                "*",
                "eq." + username,
                "eq." + password,
                1
        ).enqueue(new Callback<List<UserModel>>() {

            @Override
            public void onResponse(Call<List<UserModel>> call,
                                   Response<List<UserModel>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && !response.body().isEmpty()) {

                    // LOGIN SUCCESS
                    UserModel loggedInUser = response.body().get(0);
                    callback.onSuccess(loggedInUser);

                } else {
                    // WRONG CREDENTIALS
                    callback.onFailure("Invalid username or password");
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Log.e("SUPABASE_LOGIN", "Login failed", t);
                callback.onFailure("Network error. Please try again.");
            }
        });
    }



}

