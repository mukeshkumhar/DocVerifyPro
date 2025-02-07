package com.example.docverifypro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.docverifypro.databinding.FragmentSavedBinding
import com.google.android.material.transition.platform.MaterialSharedAxis


class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enterTransition = MaterialFadeThrough().apply { duration = 1000 }
//        exitTransition = MaterialFadeThrough().apply { duration = 1000 }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply { duration = 500 }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply { duration = 500 }

        binding.resumeSavedBtn.setOnClickListener {
            val intent = Intent(requireContext(), SavedResumeActivity::class.java)
            startActivity(intent)
        }
        binding.weblinkSavedBtn.setOnClickListener {
            val intent = Intent(requireContext(), SavedWebActivity::class.java)
            startActivity(intent)
        }

        binding.logoutBTN.setOnClickListener{
            val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("AccessToken")
            editor.apply()

            // Optional: Navigate to login screen or perform other logout actions
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
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