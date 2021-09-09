package com.android.example.ussduzmobile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.example.ussduzmobile.R
import com.android.example.ussduzmobile.databinding.FragmentLanguageBinding


class LanguageFragment : Fragment() {
    private lateinit var binding: FragmentLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageBinding.inflate(inflater, container, false)

        binding.apply {
            uz.setOnClickListener {
                findNavController().navigate(R.id.action_languageFragment_to_homeFragment)
            }
            ru.setOnClickListener {

            }
            en.setOnClickListener {

            }
        }
        return binding.root
    }


}