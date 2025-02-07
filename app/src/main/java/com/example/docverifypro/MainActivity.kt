package com.example.docverifypro

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.docverifypro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {

//                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment()).commit()
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_saved -> {


//                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, SavedFragment()).commit()
                    replaceFragment(SavedFragment())
                    true
                }
                R.id.navigation_profile -> {

//                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, ProfileFragment()).commit()
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")
        if (fragmentToLoad == "profile") {
            replaceFragment(ProfileFragment())
            binding.bottomNavigationView.selectedItemId = R.id.navigation_profile
        } else if(fragmentToLoad == "saved") {
            replaceFragment(SavedFragment())
            binding.bottomNavigationView.selectedItemId = R.id.navigation_saved
        } else {
            replaceFragment(HomeFragment())
            binding.bottomNavigationView.selectedItemId = R.id.navigation_home
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}