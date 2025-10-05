package com.example.pokeapi.ui.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeapi.databinding.ItemPlayerBinding

class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.VH>() {

    private val items = mutableListOf<RosterPlayer>()

    fun submit(list: List<RosterPlayer>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class VH(private val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: RosterPlayer) {
            binding.tvPlayerName.text = player.name
            val details = buildList {
                val jersey = player.jersey?.takeIf { it.isNotBlank() }
                if (!jersey.isNullOrBlank()) add("#${jersey}")
                player.position?.takeIf { it.isNotBlank() }?.let { add(it) }
                player.group?.takeIf { it.isNotBlank() }?.let { add(it) }
            }.joinToString(" • ")
            binding.tvPlayerDetails.text = details
        }
    }
}