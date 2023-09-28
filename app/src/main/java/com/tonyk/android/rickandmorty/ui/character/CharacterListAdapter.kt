package com.tonyk.android.rickandmorty.ui.character

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.rickandmorty.databinding.CharacterListItemBinding
import com.tonyk.android.rickandmorty.model.character.CharacterEntity

class CharacterViewHolder(
    private val binding: CharacterListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        characterEntity: CharacterEntity,
        onCharacterClicked: (character: CharacterEntity) -> Unit
    ) {
        binding.apply {
            characterGender.text = characterEntity.gender
            characterName.text = characterEntity.name
            characterSpecies.text = characterEntity.species
            characterStatus.text = characterEntity.status
            characterImage.load(characterEntity.image)

            root.setOnClickListener { onCharacterClicked(characterEntity) }
        }
        Log.d("PAgingTest333", "EPISODES NOW : ${characterEntity.location.name}")
    }
}

class CharactersListAdapter (private val onCharacterClicked: (character: CharacterEntity) -> Unit) : PagingDataAdapter<CharacterEntity, CharacterViewHolder>(
    CharacterDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CharacterViewHolder(CharacterListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characterEntity = getItem(position)
        if (characterEntity != null) {
            holder.bind(characterEntity, onCharacterClicked = onCharacterClicked)
        }
    }
}

class CharacterDiffCallback : DiffUtil.ItemCallback<CharacterEntity>() {
    override fun areItemsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
        return oldItem == newItem
    }
}