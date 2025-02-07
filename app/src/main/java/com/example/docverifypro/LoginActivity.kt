package com.example.docverifypro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.docverifypro.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

//    private val retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(RetrofitInstance.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

//    private val apiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.registerButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("AccessToken", null)

        if ( accessToken != null){
            Toast.makeText(this,"You logged in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {

            Toast.makeText(this,"You are not logged in", Toast.LENGTH_SHORT).show()
        }


        binding.loginButton.setOnClickListener{
            val email_id = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()

            val userLogin = LoginUser(email_id, password)

            ApiManager.authService.loginUser(userLogin).enqueue(object : Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful  && response.body() != null){

                        println(response.body())

                        val accessToken = response.body()?.data?.accessToken
                        println(accessToken)

                        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("AccessToken", response.body()?.data?.accessToken)
                        editor.apply()

//                        Saving User Responce in Shared Preferences

                        val userLoginSave = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        val userEditor = userLoginSave.edit()
                        userEditor.putString("user_id",response.body()?.data?.user?._id)
                        userEditor.putString("user_fullname",response.body()?.data?.user?.fullName)
                        userEditor.putString("user_name",response.body()?.data?.user?.userName)
                        userEditor.putString("user_email",response.body()?.data?.user?.email)
                        userEditor.putString("user_phone",response.body()?.data?.accessToken)
                        userEditor.apply()



                        val login = response.message()
                        Toast.makeText(this@LoginActivity,login, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("LoginActivity", "Login failed: $errorBody")
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginActivity onFailure", "Login failed: ${t.message}", t)
                    Toast.makeText(this@LoginActivity, "Login failed due to network error", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }
}



















