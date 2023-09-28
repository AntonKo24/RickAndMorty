package com.tonyk.android.rickandmorty.ui.episode

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.databinding.EpisodeListItemBinding
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity
import java.security.interfaces.ECPublicKey

class EpisodeViewHolder(
    private val binding: EpisodeListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        episodeEntity: EpisodeEntity,
        onEpisodeClicked: (episode: EpisodeEntity) -> Unit
    ) {
        binding.apply {
            episodeNameTxt.text = episodeEntity.name
            episodeNumberTxt.text = episodeEntity.episode
            airDateTxt.text = episodeEntity.air_date

            root.setOnClickListener { onEpisodeClicked(episodeEntity) }
        }
        Log.d("PAgingTest33333", "EPISODES NOW : ${episodeEntity.name}")
    }
}

class EpisodeListAdapter(private val onEpisodeClicked: (episode: EpisodeEntity) -> Unit) : PagingDataAdapter<EpisodeEntity, EpisodeViewHolder>(
    EpisodeDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EpisodeViewHolder(EpisodeListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episodeEntity = getItem(position)
        if (episodeEntity != null) {
            holder.bind(episodeEntity, onEpisodeClicked)
        }
    }
}

class EpisodeDiffCallback : DiffUtil.ItemCallback<EpisodeEntity>() {
    override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
        return oldItem == newItem
    }
}