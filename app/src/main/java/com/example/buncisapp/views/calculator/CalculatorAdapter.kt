package com.example.buncisapp.views.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.buncisapp.data.model.SoundingItems
import com.example.buncisapp.databinding.SoundingItemBinding

class CalculatorAdapter(private val data: List<SoundingItems>): RecyclerView.Adapter<CalculatorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder{
        val binding = SoundingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(private val binding: SoundingItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: SoundingItems){
            binding.tvNomorTangkiValue.text = data.nomorTanki
            binding.tvLevelSounding.text = data.levelSounding.toString()
            binding.tvVolumeValue.text = data.volume.toString()
        }
    }
}