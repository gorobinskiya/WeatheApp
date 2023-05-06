package com.example.weatheapp.presentation.week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.weatheapp.R
import com.example.weatheapp.databinding.FragmentWeekBinding
import com.example.weatheapp.presentation.MainActivity
import com.example.weatheapp.presentation.MainViewModel
import com.example.weatheapp.presentation.adapter.WeekRecyclerView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeekFragment : Fragment() {

    private lateinit var binding: FragmentWeekBinding
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeekBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        binding.weekRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            viewModel.cityFiveDayWeatherDataMap.observe(viewLifecycleOwner) { cityWeatherDataMap ->
                cityWeatherDataMap[city.name]?.let { weatherDataList ->
                    binding.cityName.text = city.name
                    binding.weekRecyclerView.adapter = WeekRecyclerView(weatherDataList)
                } ?: run {
                    binding.weekRecyclerView.adapter = WeekRecyclerView(emptyList())
                }
            }
        }
    }
}