package com.example.weatheapp.presentation.day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatheapp.R
import com.example.weatheapp.databinding.FragmentDayBinding
import com.example.weatheapp.presentation.MainActivity
import com.example.weatheapp.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayFragment : Fragment() {
    private lateinit var binding: FragmentDayBinding
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            binding.city.text = city.name
            binding.degreesNum.text = city.temp.toString()
            binding.pressureValue.text = city.pressure.toString()
            binding.windValue.text = city.windSpeed.toString()
        }
    }


}