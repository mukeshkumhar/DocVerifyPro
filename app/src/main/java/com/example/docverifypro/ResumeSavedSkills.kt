package com.example.docverifypro

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.docverifypro.databinding.ActivityResumeSavedSkillsBinding

class ResumeSavedSkills : AppCompatActivity() {

    private lateinit var binding: ActivityResumeSavedSkillsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResumeSavedSkillsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.backBTN.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.saveButton.setOnClickListener{
            val intent = Intent(this, ScanResumeActivity::class.java)
            startActivity(intent)
        }
//        val autoCompleteTextView: AutoCompleteTextView = binding.savedSkill

        val items = arrayOf("Item 1", "Item 2", "Item 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)

//        autoCompleteTextView.setAdapter(adapter)
//        autoCompleteTextView.threshold = 1
    }
}