package com.tonyk.android.rickandmorty.ui.character

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.rickandmorty.databinding.CharactersListItemBinding
import com.tonyk.android.rickandmorty.model.CharacterEntity

class CharacterViewHolder(
    private val binding: CharactersListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        characterEntity: CharacterEntity
    ) {
        binding.characterGender.text = characterEntity.gender
        binding.characterName.text = characterEntity.name
        binding.characterSpecies.text = characterEntity.species
        binding.characterStatus.text = characterEntity.status
        binding.characterImage.load(characterEntity.image)
        Log.d("PAgingTest", "ID NOW : ${characterEntity.id}")
    }
}

class CharactersListAdapter : PagingDataAdapter<CharacterEntity, CharacterViewHolder>(
    ContactDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CharacterViewHolder(CharactersListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characterEntity = getItem(position)
        if (characterEntity != null) {
            holder.bind(characterEntity)
        }
    }
}

class ContactDiffCallback : DiffUtil.ItemCallback<CharacterEntity>() {
    override fun areItemsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
        return oldItem == newItem
    }
}