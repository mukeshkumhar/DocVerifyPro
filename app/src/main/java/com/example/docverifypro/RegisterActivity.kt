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
import com.example.docverifypro.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener{
            val full_name = binding.fullName.text.toString()
            val user_name = binding.userName.text.toString()
            val email_id = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()

            val registerRequest = RegisterUser(full_name, user_name, email_id, password)

            ApiManager.authService.registerUser(registerRequest).enqueue(object : Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful  && response.body() != null){
                        val accessToken = response.body()?.tokens?.accessToken
                        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("AccessToken", accessToken)
                        editor.apply()

                        val userRegisterSave = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        val userEditor = userRegisterSave.edit()
                        userEditor.putString("user_id",response.body()?.user?._id)
                        userEditor.putString("user_fullname",response.body()?.user?.fullName)
                        userEditor.putString("user_name",response.body()?.user?.userName)
                        userEditor.putString("user_email",response.body()?.user?.email)
                        userEditor.apply()

                        Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }  else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("RegistrationActivity", "Login failed: $errorBody")
                        Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e("RegisterActivity onFailure", "Registration failed: ${t.message}", t)
                    Toast.makeText(this@RegisterActivity, "Registration failed due to network error", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }
}