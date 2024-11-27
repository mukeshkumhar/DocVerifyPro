package com.example.docverifypro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.button.MaterialButton


class HomeFragment : Fragment() {

    fun onViewCreate(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resumeButton: MaterialButton = view.findViewById(R.id.resumecheck)
        val webButton: MaterialButton = view.findViewById(R.id.webcheck)

        resumeButton.setOnClickListener{
            val intent = Intent(requireContext(), ResumeSavedSkills::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

}