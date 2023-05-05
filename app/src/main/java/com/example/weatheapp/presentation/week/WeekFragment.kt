package com.example.weatheapp.presentation.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatheapp.R
import com.example.weatheapp.databinding.FragmentWeekBinding


class WeekFragment : Fragment(R.layout.fragment_week) {
    private lateinit var binding: FragmentWeekBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeekBinding.inflate(inflater, container, false)

        return binding.root

    }


}