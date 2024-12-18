package com.example.docverifypro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.docverifypro.databinding.FragmentHome2Binding
import com.example.docverifypro.databinding.FragmentSavedBinding


class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.resumeSavedBtn.setOnClickListener {
            val intent = Intent(requireContext(), SavedResumeActivity::class.java)
            startActivity(intent)
        }
        binding.weblinkSavedBtn.setOnClickListener {
            val intent = Intent(requireContext(), SavedWebActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}