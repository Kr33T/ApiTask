package com.example.apitask;

import android.provider.ContactsContract;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @GET("apps/{id}")
    Call<DataModal> getData(@Query("id") int id);

    @POST("apps")
    Call<DataModal> createPost(@Body DataModal dataModal);

    @PUT("apps/{id}")
    Call<DataModal> updateData(@Path("id") int id, @Body DataModal dataModal);

    @DELETE("apps/{id}")
    Call<Void> deleteData(@Path("id") int id);
}