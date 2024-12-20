package com.example.docverifypro

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import com.example.docverifypro.databinding.ActivityScanResumeBinding
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream

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

        binding.backBTN.setOnClickListener{
            val intent = Intent(this,ResumeSavedSkills::class.java)
            startActivity(intent)
        }


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

        // Text Extraction and Display
        binding.scanPdf.setOnClickListener {
            if (selectedFiles.isNotEmpty()) {
                val extractedText = StringBuilder()
                for (uri in selectedFiles) {
                    val filePath = getFileFromUri(uri) // Implement this function to get file path from Uri
                    if (filePath != null) {
                        extractedText.append(extractTextFromFile(filePath)).append("\n\n")
                    }
                }
                binding.extractedText.text = extractedText.toString()
            } else {
                Toast.makeText(this, "Please select files first", Toast.LENGTH_SHORT).show()
            }
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