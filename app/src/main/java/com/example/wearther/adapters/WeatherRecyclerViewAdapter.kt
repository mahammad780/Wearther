package com.example.wearther.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wearther.R
import com.example.wearther.databinding.ListItemBinding
import com.example.wearther.model.WeatherModel
import com.squareup.picasso.Picasso

class WeatherRecyclerViewAdapter(val listener: ItemClickListener?) :
    ListAdapter<WeatherModel, WeatherRecyclerViewAdapter.MyViewHolder>(Comparator()) {
    class MyViewHolder(view: View, val listener: ItemClickListener?) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)
        var itemTemp: WeatherModel? = null
        init{
            itemView.setOnClickListener {
                itemTemp?.let { it1 -> listener?.onClick(it1) }
            }
        }

        fun bind(item: WeatherModel) {
            itemTemp = item
            binding.dateItemTextView.text = item.time
            binding.conditionTextView.text = item.condition
            binding.temperatureTextView.text = item.currentTemperature.ifEmpty{"${item.maxTemp.toDoubleOrNull()?.toInt().toString()}°C/" +
                    "${item.minTemp.toDoubleOrNull()?.toInt().toString()}°C"}
            Picasso.get().load("https:" + item.imageURL).into(binding.conditionImageView)
        }

    }

    class Comparator : DiffUtil.ItemCallback<WeatherModel>() {
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface ItemClickListener{
        fun onClick(item: WeatherModel)
    }
    
}