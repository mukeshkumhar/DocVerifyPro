package com.example.docverifypro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.docverifypro.databinding.ActivityScanResumeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanResumeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResumeBinding
    private val fileQueue = mutableListOf<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backBTN.setOnClickListener{
            val intent = Intent(this,ResumeSavedSkills::class.java)
            startActivity(intent)
        }

        binding.addResume.setOnClickListener{
            openFilePicker()
        }

    }
    // File Picker
    private val pickMultipleFiles = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                if (data.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val uri = data.clipData!!.getItemAt(i).uri
                        fileQueue.add(uri)
                    }
                } else if (data.data != null) {
                    val uri = data.data!!
                    fileQueue.add(uri)
                }
                processNextFile() // Start processing
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*" // Or specify PDF and DOCX types
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickMultipleFiles.launch(intent)
    }

    // File Processing
    private fun processNextFile() {
        if (fileQueue.isNotEmpty()) {
            val currentFileUri = fileQueue.removeAt(0)
            processFile(currentFileUri)
        } else {
            Toast.makeText(this, "All resumes scanned", Toast.LENGTH_SHORT).show()
        }
    }
    private fun processFile(uri: Uri) {
        // 1. Read file content (using ContentResolver or other methods)
//        val fileContent = readFileContent(uri) // Implement this function

        // 2. Send file content to backend (using Retrofit or other methods)
//        apiService.uploadFile(fileContent).enqueue(object : Callback<ApiResponse> {
//            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                if (response.isSuccessful) {
//                    // 3. Store response in MongoDB (using Room or other methods)
//                    // ...
//                    processNextFile() // Process the next file
//                } else {
//                    // Handle error
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                // Handle network error
//            }
//        })
    }
    // Helper function to read file content (implement as needed)
//    private fun readFileContent(uri: Uri): ByteArray {
//        // ... (Implementation to read file content from Uri) ...
//    }
//



}