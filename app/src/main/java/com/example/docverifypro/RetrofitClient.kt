package com.example.docverifypro

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users/register") fun registerUser(@Body user: RegisterUser): Call<RegisterResponse>
    @POST("users/login") fun loginUser(@Body user: LoginUser): Call<LoginResponse>
}