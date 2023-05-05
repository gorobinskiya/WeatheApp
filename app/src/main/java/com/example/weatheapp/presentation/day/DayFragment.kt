package com.example.weatheapp.presentation.day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatheapp.R
import com.example.weatheapp.databinding.FragmentDayBinding


class DayFragment : Fragment(R.layout.fragment_day) {
    private lateinit var binding: FragmentDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayBinding.inflate(inflater, container, false)

        return binding.root

    }


}