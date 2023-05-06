package com.example.weatheapp.presentation.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.weatheapp.R

class CitySelectionDialog(context: Context, private val onCitySelected: (String) -> Unit) :
    AlertDialog.Builder(context) {

    init {
        val cities = context.resources.getStringArray(R.array.cities)

        setTitle(context.getString(R.string.select_city))
        setItems(cities) { _, which ->
            val selectedCity = cities[which]
            onCitySelected(selectedCity)
        }
        setNegativeButton(context.getString(R.string.cancel), null)
    }
}