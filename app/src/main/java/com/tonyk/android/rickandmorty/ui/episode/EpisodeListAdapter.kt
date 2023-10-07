package com.tonyk.android.rickandmorty.ui.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.EpisodeListItemBinding
import com.tonyk.android.rickandmorty.model.episode.EpisodeEntity

class EpisodeViewHolder(
    private val binding: EpisodeListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        episodeEntity: EpisodeEntity,
        onEpisodeClicked: (episode: EpisodeEntity) -> Unit
    ) {
        binding.apply {
            episodeNameTxt.text =
                root.context.getString(R.string.episode_name, episodeEntity.name)
            episodeNumberTxt.text =
                root.context.getString(R.string.episode_number_fill, episodeEntity.episode)
            airDateTxt.text =
                root.context.getString(R.string.episode_date, episodeEntity.air_date)

            root.setOnClickListener { onEpisodeClicked(episodeEntity) }
        }
    }
}

class EpisodeListAdapter(private val onEpisodeClicked: (episode: EpisodeEntity) -> Unit) :
    PagingDataAdapter<EpisodeEntity, EpisodeViewHolder>(
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