package com.example.fairshare;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseAPI {

    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im10c2x0cmtvaW5qcXltcWlhbHphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4OTg4OTEsImV4cCI6MjA4MTQ3NDg5MX0.LRryc486y-hu3YWe6TCH_4WT2CCJKmfrJCa3AVh2Nno",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im10c2x0cmtvaW5qcXltcWlhbHphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4OTg4OTEsImV4cCI6MjA4MTQ3NDg5MX0.LRryc486y-hu3YWe6TCH_4WT2CCJKmfrJCa3AVh2Nno",
            "Content-Type: application/json"
    })
    @GET("rest/v1/users")
    Call<List<UserModel>> loginUser(
            @Query("select") String select,
            @Query("username") String usernameEq,
            @Query("password") String passwordEq,
            @Query("limit") int limit
    );

    @Headers({
            "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im10c2x0cmtvaW5qcXltcWlhbHphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4OTg4OTEsImV4cCI6MjA4MTQ3NDg5MX0.LRryc486y-hu3YWe6TCH_4WT2CCJKmfrJCa3AVh2Nno",
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im10c2x0cmtvaW5qcXltcWlhbHphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU4OTg4OTEsImV4cCI6MjA4MTQ3NDg5MX0.LRryc486y-hu3YWe6TCH_4WT2CCJKmfrJCa3AVh2Nno",
            "Content-Type: application/json",
            "Prefer: return=minimal"
    })
    @POST("rest/v1/users")
    Call<ResponseBody> registerUser(@Body UserModel user);
}
