package com.example.docverifypro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class SavedFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resumeButton: ImageButton = view?.findViewById(R.id.resume_saved_btn)!!
        val webButton: ImageButton = view?.findViewById(R.id.weblink_saved_btn)!!

        resumeButton.setOnClickListener{
            val intent = Intent(requireContext(), SavedResumeActivity::class.java)
            startActivity(intent)
        }
        webButton.setOnClickListener{
            val intent = Intent(requireContext(), SavedWebActivity::class.java)
        }

    }


}