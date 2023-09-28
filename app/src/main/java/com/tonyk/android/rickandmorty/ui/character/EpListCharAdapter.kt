package com.tonyk.android.rickandmorty.ui.character

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.databinding.EpisodeListItemBinding
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import androidx.recyclerview.widget.ListAdapter

import coil.load
import com.tonyk.android.rickandmorty.model.character.CharacterEntity


class EpListVH(
    private val binding: EpisodeListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        episodeEntity: EpisodeEntity,
        onEpisodeClicked: (episodeEntity: EpisodeEntity) -> Unit
    ) {
        binding.apply {
            episodeNameTxt.text = episodeEntity.name
            episodeNumberTxt.text = episodeEntity.episode
            airDateTxt.text = episodeEntity.air_date

            root.setOnClickListener { onEpisodeClicked(episodeEntity) }
        }
    }
}

class EpListCharAdapter(private val onEpisodeClicked: (episodeEntity: EpisodeEntity) -> Unit) : ListAdapter<EpisodeEntity, EpListVH>(ContactDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpListVH {
        val inflater = LayoutInflater.from(parent.context)
        return EpListVH(EpisodeListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: EpListVH, position: Int) {
        val contact = getItem(position)
        holder.bind(contact, onEpisodeClicked)
    }
}

class ContactDiffCallback : DiffUtil.ItemCallback<EpisodeEntity>() {
    override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem == newItem
    }
}
