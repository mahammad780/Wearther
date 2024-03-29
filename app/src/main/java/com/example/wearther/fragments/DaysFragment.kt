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
import com.example.wearther.databinding.FragmentDaysBinding
import com.example.wearther.model.WeatherModel


class DaysFragment : Fragment(), WeatherRecyclerViewAdapter.ItemClickListener {
    private lateinit var binding: FragmentDaysBinding
    private lateinit var adapter: WeatherRecyclerViewAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaysBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        model.liveDataList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun initRecyclerView(){
        adapter = WeatherRecyclerViewAdapter(this)
        binding.recyclerViewDays.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewDays.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }

    override fun onClick(item: WeatherModel) {
        model.liveDataCurrent.value = item
    }
}