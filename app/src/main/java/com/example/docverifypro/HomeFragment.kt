package com.example.docverifypro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.docverifypro.databinding.FragmentHome2Binding
import android.widget.ImageButton
import com.example.docverifypro.databinding.FragmentProfileBinding
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {
    private var _binding: FragmentHome2Binding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.resumecheck.setOnClickListener{
            val intent = Intent(requireContext(), ScanResumeActivity::class.java)
            startActivity(intent)
        }

        binding.webcheck.setOnClickListener {
            val intent = Intent(requireContext(), ScanWebActivity::class.java)
            startActivity(intent)
        }
        binding.qrCode.setOnClickListener{
            val intent = Intent(requireContext(), QRCode::class.java)
            startActivity(intent)
        }
        binding.qrCodeGenerate.setOnClickListener{
            val intent = Intent(requireContext(), QRCodeGenerater::class.java)
            startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", null)
        binding.userName.text = userName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHome2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}