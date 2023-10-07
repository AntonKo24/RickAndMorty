package com.tonyk.android.rickandmorty.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.FragmentLocationDetailsBinding
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.ui.base.BaseDetailsFragment
import com.tonyk.android.rickandmorty.ui.character.CharacterViewHolder
import com.tonyk.android.rickandmorty.ui.character.CharactersListAdapter
import com.tonyk.android.rickandmorty.viewmodel.location.LocationDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationDetailsFragment : BaseDetailsFragment<CharacterEntity, CharacterViewHolder>() {
    private var _binding: FragmentLocationDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: LocationDetailsFragmentArgs by navArgs()

    override val viewModel: LocationDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailsFragment(args.id)

    }

    override fun createAdapter(): PagingDataAdapter<CharacterEntity, CharacterViewHolder> {
        return CharactersListAdapter(
            onCharacterClicked = {
                findNavController().navigate(LocationDetailsFragmentDirections.toCharacterDetails(it.id))
            }
        )
    }

    override fun setupUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.location.collectLatest {
                binding.apply {
                    locationName.text = getString(R.string.location_name, it.name)
                    locationtypeTxt.text = getString(R.string.location_type, it.type)
                    dimensionTxt.text = getString(R.string.location_dimension, it.dimension)
                    val residents = it.residents
                    if (it.id != -1 && residents[0].isEmpty()) {
                        progressBar.isVisible = false
                        emptyStateText.isVisible = true
                    } // can explain why it's here
                }
            }
        }
        binding.apply {
            SwipeRefreshLayout.setOnRefreshListener {
                refreshFragmentData(args.id)
                SwipeRefreshLayout.isRefreshing = false
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setupAdapter(adapter: PagingDataAdapter<CharacterEntity, CharacterViewHolder>) {
        binding.apply {
            locationCharList.adapter = adapter
            locationCharList.layoutManager = GridLayoutManager(context, 2)
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.apply {
                    progressBar.isVisible =
                        loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
                    emptyStateText.isVisible = loadStates.refresh is LoadState.Error
                    if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                        emptyStateText.isVisible = adapter.itemCount < 1
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}