package com.example.docverifypro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docverifypro.databinding.ActivitySavedResumeBinding
import com.example.docverifypro.databinding.ActivityScanResumeBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SavedResumeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedResumeBinding
    private var rsumeMongo = ArrayList<ResumeMongoDB>()
    private var resumeMD = ArrayList<ResumeMongoData>()
    private var projectMD = ArrayList<MongoProject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySavedResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        binding.backBTN.setOnClickListener{
//            val intent = Intent(this,SavedFragment::class.java)
//            startActivity(intent)
//        }

        // In SavedResumeActivity:
        binding.backBTN.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.saved_fragment_page, SavedFragment())
                .addToBackStack(null)
                .commit()
            finish()
        }

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, SavedResumeActivity())
//            .addToBackStack(null)
//            .commit()
//
//        binding.backBTN.setOnClickListener {
//            supportFragmentManager.popBackStack()
//        }

        val recyclerView: RecyclerView = binding.resumeRecycleView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val authInterceptor = AuthInterceptor(sharedPreferences)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInstance.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiServiceWithInterceptor = retrofit.create(ApiService::class.java)

        apiServiceWithInterceptor.getAllResume().enqueue(object : Callback<ResumeMongoDB>{
            override fun onResponse(p0: Call<ResumeMongoDB>, p1: Response<ResumeMongoDB>) {
                if(p1.isSuccessful && p1.body() != null){
//                    resumeMD = p1.body()?.data
//                    projectMD = p1.body()?.data as ArrayList<MongoProject>
                    println(p1.body())
                    val resumeMongoDBResponse = p1.body()

                    val resumeAdapter = ResumeRecycleViewAdapter(resumeMongoDBResponse!!.data)
                    recyclerView.adapter = resumeAdapter

//                    val projectAdapter = ProjectAdapter(projectMD)
//                    recyclerView.adapter = projectAdapter
                }
            }

            override fun onFailure(p0: Call<ResumeMongoDB>, t: Throwable) {
                Log.e("SavedResumeActivity", "Getting resume failed: ${t.message}", t)
                Toast.makeText(this@SavedResumeActivity, "Getting resume failed due to network error", Toast.LENGTH_SHORT).show()
            }

        })

    }
}