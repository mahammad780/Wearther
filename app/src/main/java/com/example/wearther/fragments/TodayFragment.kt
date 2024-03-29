package com.example.wearther.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wearther.MainViewModel
import com.example.wearther.adapters.WeatherRecyclerViewAdapter
import com.example.wearther.databinding.FragmentTodayBinding
import com.example.wearther.model.WeatherModel
import org.json.JSONArray
import org.json.JSONObject

class TodayFragment : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    private lateinit var adapter: WeatherRecyclerViewAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        model.liveDataCurrent.observe(viewLifecycleOwner){
            adapter.submitList(getHoursList(it))
        }
    }

    private fun initRecyclerView(){
        adapter = WeatherRecyclerViewAdapter(null)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun getHoursList(item: WeatherModel): List<WeatherModel> {
        val hoursArray = JSONArray(item.hours)
        val list = ArrayList<WeatherModel>()
        for (i in 0 until hoursArray.length()) {
            val it = WeatherModel(
                item.city, (hoursArray[i] as JSONObject).getString("time"),
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("text"),
                (hoursArray[i] as JSONObject).getString("temp_c"), "", "",
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
                ""
            )
            list.add(it)
        }
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() = TodayFragment()
    }
}
