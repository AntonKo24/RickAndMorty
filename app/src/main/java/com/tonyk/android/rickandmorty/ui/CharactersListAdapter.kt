package com.tonyk.android.rickandmorty.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.rickandmorty.databinding.CharactersListItemBinding
import com.tonyk.android.rickandmorty.model.Character


class CharacterViewHolder(
    private val binding: CharactersListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        character: Character
    ) {
        binding.characterGender.text = character.gender
        binding.characterName.text = character.name
    }
}

class CharactersListAdapter(
) : ListAdapter<Character, CharacterViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CharacterViewHolder(CharactersListItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }
}

class ContactDiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}