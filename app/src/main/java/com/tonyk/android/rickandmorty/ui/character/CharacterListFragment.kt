package com.tonyk.android.rickandmorty.ui.character

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.ui.BaseListFragment
import com.tonyk.android.rickandmorty.viewmodel.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterListFragment : BaseListFragment<CharacterEntity, CharacterViewHolder>() {

    override val viewModel: CharactersListViewModel by activityViewModels()

    override fun createAdapter(): PagingDataAdapter<CharacterEntity, CharacterViewHolder> {
        return CharactersListAdapter(
            onCharacterClicked = { character ->
                findNavController().navigate(
                    CharacterListFragmentDirections.toCharacterDetail(
                        character
                    )
                )
            }
        )
    }

    override fun navigateToFilterFragment() {
        findNavController().navigate(CharacterListFragmentDirections.toCharactersFilterFragment())
    }

}
