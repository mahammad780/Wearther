package com.example.wearther.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wearther.MainViewModel
import com.example.wearther.adapters.ViewPagerAdapter
import com.example.wearther.databinding.FragmentMainBinding
import com.example.wearther.model.WeatherModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API_KEY = "5a1f3144efec4ea8a7e193231232411"

class MainFragment : Fragment() {
    private val fragmentList = listOf(
        TodayFragment.newInstance(), DaysFragment.newInstance()
    )
    private val tList = listOf("Hours", "Days")
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()
        updateCurrentCard()
        requestWeatherData("Berlin")
    }

    private fun init() {
        val adapter = ViewPagerAdapter(activity as FragmentActivity, fragmentList)
        binding.mainFragmentViewPaiger.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.mainFragmentViewPaiger) { tab, pos ->
            tab.text = tList[pos]
        }.attach()
    }

    private fun permissionListener() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                Toast.makeText(activity, "Permision is $it", Toast.LENGTH_LONG).show()
            }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun requestWeatherData(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY + "" +
                "&q=" +
                city +
                "&days=" +
                "10" +
                "&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(Request.Method.GET, url, { result -> parseWeatherData(result)},
            { error -> Log.d("Mylog", "Error: $error") })
        queue.add(request)
    }

    private fun parseCurrentWeather(mainObject: JSONObject, weatherItem: WeatherModel) {
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getString("temp_c"),
            weatherItem.maxTemp,
            weatherItem.minTemp,
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )
        model.liveDataCurrent.value = item
    }

    private fun parseDaysWeather(mainObject: JSONObject): List<WeatherModel> {
        val list = ArrayList<WeatherModel>()
        val daysArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val name = mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val item = WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                "",
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        model.liveDataList.value = list
        return list
    }

    private fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val listDays = parseDaysWeather(mainObject)
        parseCurrentWeather(mainObject, listDays[0])
    }

    private fun updateCurrentCard() {
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            val maxMinTemperature = "${it.maxTemp.toDoubleOrNull()?.toInt().toString()}°C/${it.minTemp.toDoubleOrNull()?.toInt().toString()}°C"
            val currentTemperature = it.currentTemperature.toDoubleOrNull()?.toInt().toString() + "°C"
            binding.textDateAndTime.text = it.time
            binding.cityName.text = it.city
            binding.textDegree.text = if(!it.currentTemperature.isEmpty()){currentTemperature} else{maxMinTemperature}
            binding.textWeatherCondition.text = it.condition
            binding.minMaxTemperature.text = if(it.currentTemperature.isEmpty()){""} else{maxMinTemperature}
            Picasso.get().load("https:" + it.imageURL).into(binding.imageWeatherCondition)
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}