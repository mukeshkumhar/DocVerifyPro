package com.example.docverifypro

import android.content.SharedPreferences

class UserReposetry(private val apiService: ApiService, private val sharedPreferences: SharedPreferences) {
    suspend fun login(request: LoginUser): Result<LoginResponse> {
        return try {
            val response = apiService.loginUser(request).execute()
            if (response.isSuccessful) {
                val loginResponse = response.body()!!
                val accessToken = loginResponse.data.accessToken
                with(sharedPreferences.edit()) {
                    putString("access_token", accessToken)
                    apply()
                }
                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}