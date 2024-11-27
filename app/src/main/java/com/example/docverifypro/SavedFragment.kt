package com.example.docverifypro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class SavedFragment : Fragment() {


    fun onViewCreate(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resumeButton: ImageButton= view.findViewById(R.id.resume_saved_btn)
        val webButton: ImageButton= view.findViewById(R.id.weblink_saved_btn)

        resumeButton.setOnClickListener{
            val intent = Intent(requireContext(), SavedResumeActivity::class.java)
            startActivity(intent)
        }
        webButton.setOnClickListener{
            val intent = Intent(requireContext(), SavedWebActivity::class.java)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }


}