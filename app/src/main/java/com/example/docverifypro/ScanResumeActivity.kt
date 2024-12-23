package com.example.docverifypro

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import com.example.docverifypro.databinding.ActivityScanResumeBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.io.FileInputStream


data class ResumeData(
    val name: String?,
    val contact: String?,
    val email: String?,
    val address: String?,
    val percentage: Double,
    val projects: List<Project>
)

data class Project(
    val name: String?,
    val summary: String?
)



class ScanResumeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResumeBinding
    private val selectedFiles = mutableListOf<Uri>()

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

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        println("AccessToken Checking -> ${sharedPreferences.getString("AccessToken", null)}")
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

        binding.backBTN.setOnClickListener{
            finish()
        }

        binding.logoutBTN.setOnClickListener{
            val sharedPreferences = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("AccessToken")
            editor.apply()

            // Optional: Navigate to login screen or perform other logout actions
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        val loadingText = binding.loadingText
        val progressBar = binding.progressBar




        // File Picker
        val pickMultipleFiles = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    selectedFiles.clear() // Clear previous selections
                    if (data.clipData != null) {
                        val count = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val uri = data.clipData!!.getItemAt(i).uri
                            selectedFiles.add(uri)
                        }
                    } else if (data.data != null) {
                        val uri = data.data!!
                        selectedFiles.add(uri)
                    }
                    binding.selectedFilesCount.text = "Selected files: ${selectedFiles.size}"
                }
            }
        }

        binding.addResume.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*" // Or specify PDF and DOCX types
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            pickMultipleFiles.launch(intent)
        }
        fun isResumeRelated(prompt: String): Boolean{
            val resumeKeywords = listOf("resume",
                "education",
                "experience",
                "skills",
                "projects",
                "certifications",
                "achievements",
                "Experiences")
            return resumeKeywords.any{ prompt.contains(it, ignoreCase = true)}
        }

        // Text Extraction and Display
        binding.scanPdf.setOnClickListener {
            loadingText.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            if (selectedFiles.isNotEmpty()) {
                val extractedText = StringBuilder()
                for (uri in selectedFiles) {
                    val filePath = getFileFromUri(uri) // Implement this function to get file path from Uri
                    if (filePath != null) {
                        extractedText.append(extractTextFromFile(filePath)).append("\n\n")
                    }
                }
                binding.extractedText.text = extractedText.toString()
                val pdftext = extractedText.toString()
                val mySkills = binding.newSkills.text.toString()
                val myPrompt = "This is resume, extract data from these and give me result in json format i want name,contact, email, address, percentage of skill matched in double datatype, projects in array with name and summary of projects, only give this data in json format. I am giving you skills and language list match with given resume skills and language and give me result in json my skill are = " + mySkills + "Give me in Json format {name,contact, email, address, percentage, project[name,summary]}"

                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = "AIzaSyAuc9u6UAiav9EVSydimqcYtn8lStBxEGU"
                )

                if (isResumeRelated(pdftext)){
                    MainScope().launch {
                        val response = generativeModel.generateContent(pdftext + "\n" + myPrompt)
                        val responseText = response.text
                        val startIndex = responseText?.indexOf("{")
                        val lastIndex = responseText?.lastIndexOf("}")
                        val jsonText = responseText?.substring(startIndex!!,lastIndex!!)
                        val jsonNew = (jsonText)
                        val jsonFinal = (jsonNew+"}")
                        print(jsonFinal)
//                val jsonString = """
//                    {
//                      "name": "MUKESH KUMHAR",
//                      "contact": "+91 9064784636",
//                      "email": "mukeshkumhar906@gmail.com",
//                      "address": "Ranchi, Jharkhand",
//                      "skill_match_percentage": 66.67,
//                      "projects": [
//                      {
//                        "name": "Project 1",
//                        "summary": "Description of Project 1"
//                        }
//                      ]
//                    }
//                """
//                print(jsonString)


                        val gson = Gson()
                        val user: ResumeData = gson.fromJson(jsonFinal, ResumeData::class.java)
                        val name = user.name.toString()
                        val contact = user.contact.toString()
                        val email = user.email.toString()
                        val address = user.address.toString()
                        val percentage = user.percentage

                        val inputResume = ResumeCreate(name, contact, email, address, percentage)

                        val threshold = binding.thresholdNo.text.toString().toDouble()

                        if(percentage >= threshold){

                            apiServiceWithInterceptor.createResume(inputResume).enqueue(object :
                                Callback<ResumeResponse>{
                                override fun onResponse(
                                    call: Call<ResumeResponse>,
                                    response: Response<ResumeResponse>
                                ) {
                                    if(response.isSuccessful && response.body() != null){

                                        println(response.body())

                                        Toast.makeText(this@ScanResumeActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                                        binding.output.setText(user.name+"\n"+user.contact+"\n"+user.email+"\n"+user.address+"\n"+user.percentage)
                                        val resumeId = response.body()?.data?._id
                                        user.projects.forEachIndexed{index,project ->
                                            val projectName = project.name.toString()
                                            val projectSummary = project.summary.toString()
                                            val addProject = ProjectCreated(projectName, projectSummary, resumeId.toString())

//                                          calling api to add projects in resume
                                            apiServiceWithInterceptor.addProjects(addProject).enqueue(object : Callback<ProjectResponse>{
                                                override fun onResponse(
                                                    call: Call<ProjectResponse>,
                                                    response: Response<ProjectResponse>
                                                ) {
                                                    if(response.isSuccessful && response.body() != null){
//                                                        Toast.makeText(this@ScanResumeActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                                                        println("Project Added -> "+response.body())

                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<ProjectResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("ScanResumeActivity onFailure", "Adding Project failed: ${t.message}", t)
                                                    Toast.makeText(this@ScanResumeActivity, "Adding Project failed due to network error", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }
                                    } else {
                                        val errorBody = response.errorBody()?.string()
                                        Log.e("ScanResumeActivity", "Create resume failed: $errorBody")
                                        Toast.makeText(this@ScanResumeActivity, "Create resume failed", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<ResumeResponse>, t: Throwable) {
                                    Log.e("ScanResumeActivity onFailure", "Saving resume failed: ${t.message}", t)
                                    Toast.makeText(this@ScanResumeActivity, "Save resume failed due to network error", Toast.LENGTH_SHORT).show()
                                }

                            })

                        } else {
                            binding.output.setText("Resume does not match with threshold")
                        }




//                val resumeData = Json.decodeFromString<ResumeData>(jsonString)
//                val name = resumeData.name
//                val contact = resumeData.contact
//                val email = resumeData.email
//                val address = resumeData.address

                        // Append project details
                        val projectsText = StringBuilder("Projects:\n")
                        user.projects.forEachIndexed { index, project ->
                            projectsText.append("\n${index + 1}. ${project.name}\n")
                            projectsText.append("   ${project.summary}\n")
                        }
                        binding.project.text = projectsText.toString()
                        loadingText.visibility = View.GONE
                        progressBar.visibility = View.GONE
                    }
                }else{
                    binding.output.setText("I will give answer only resume related questions")
                }
                loadingText.visibility = View.GONE
                progressBar.visibility = View.GONE

            } else {
                Toast.makeText(this, "Please select files first", Toast.LENGTH_SHORT).show()
            }
            loadingText.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

    }


    // Helper functions for text extraction
    private fun extractTextFromFile(filePath: String): String {
        return when {
            filePath.endsWith(".pdf") -> extractTextFromPdf(filePath)
            filePath.endsWith(".docx") -> extractTextFromDocx(filePath)
            else -> ""
        }
    }

    private fun extractTextFromPdf(pdfFilePath: String): String {
        val reader = PdfReader(pdfFilePath)
        val stringBuilder = StringBuilder()
        for (i in 1..reader.numberOfPages) {
            stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, i))
        }
        reader.close()
        return stringBuilder.toString()
    }

    private fun extractTextFromDocx(docxFilePath: String): String {
        val fis = FileInputStream(docxFilePath)
        val document = XWPFDocument(fis)
        val extractor = XWPFWordExtractor(document) // Make sure to import XWPFWordExtractor
        val text = extractor.text
        extractor.close()
        fis.close()
        return text
    }

    // Implement getFileFromUri() to get file path from Uri
    private fun getFileFromUri(uri: Uri): String? {
        // ... (Implementation to get file path from Uri) ...
        // Check if the Uri is a file Uri
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            return uri.path
        }

        // If not a file Uri, try to get the path from the content resolver
        var filePath: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    val fileName = it.getString(columnIndex)
                    val file = File(cacheDir, fileName)
                    filePath = file.absolutePath

                    // Copy the file to the cache directory
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }
            }
        }

        return filePath
    }



}