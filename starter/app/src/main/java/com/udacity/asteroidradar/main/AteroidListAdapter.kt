package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListitemBinding


class AsteroidAdapter(val clickListener: AsteroidClickListener) : ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val itemDataBinding : AsteroidListitemBinding) : RecyclerView.ViewHolder(itemDataBinding.root) {

        fun bind(item: Asteroid) {
            itemDataBinding.asteroid = item
            itemDataBinding.clickListener = clickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val withDataBinding: AsteroidListitemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.asteroid_listitem, parent, false)
        return ViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
class AsteroidClickListener (val itemClick: (item : Asteroid) -> Unit) {
    fun onClick(asteroidItem: Asteroid) = itemClick(asteroidItem)

}

