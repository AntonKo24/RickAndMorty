package com.tonyk.android.rickandmorty.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.tonyk.android.rickandmorty.databinding.FragmentLocationDetailsBinding
import com.tonyk.android.rickandmorty.ui.character.CharactersListAdapter
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.location.LocationDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    private var _binding: FragmentLocationDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: LocationDetailsFragmentArgs by navArgs()
    private val locationDetailsViewmodel: LocationDetailsViewModel by viewModels()
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


        initializeFragment()

        setupUi()

        setupAdapter()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeFragment() {
        val status = NetworkChecker.isNetworkAvailable(requireContext())
        locationDetailsViewmodel.initializeDetailsFragment(status, args.id)
    }

    private fun setupUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            locationDetailsViewmodel.location.collectLatest {
                binding.apply {
                    locationName.text = it.name
                    locationtypeTxt.text = it.type
                    dimensionTxt.text = it.dimension
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
                refreshData()
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupAdapter() {
        val adapter = CharactersListAdapter(
            onCharacterClicked = {
                findNavController().navigate(LocationDetailsFragmentDirections.toCharacterDetails(it.id))
            }
        )
        binding.apply {
            locationCharList.adapter = adapter
            locationCharList.layoutManager = GridLayoutManager(context, 2)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationDetailsViewmodel.dataFlow.collect { data ->
                    adapter.submitData(data)

                }
            }
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

    private fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        locationDetailsViewmodel.refreshDetailsFragment(statusRefreshed, args.id)
        binding.SwipeRefreshLayout.isRefreshing = false
    }
}