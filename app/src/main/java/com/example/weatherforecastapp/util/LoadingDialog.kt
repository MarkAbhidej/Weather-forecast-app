package com.example.weatherforecastapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.weatherforecastapp.databinding.DialogLoadingBinding

class LoadingDialog : DialogFragment() {
    private lateinit var binding: DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogLoadingBinding.inflate(inflater)
        return binding.root
    }
}