package com.example.docverifypro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.docverifypro.databinding.FragmentHome2Binding
import com.example.docverifypro.databinding.FragmentProfileBinding
import com.google.android.material.transition.platform.MaterialSharedAxis


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply { duration = 500 }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply { duration = 500 }

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val fullName = sharedPreferences.getString("user_fullname", null)

            binding.userNameText.text = fullName

            binding.logoutButton.setOnClickListener{
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}