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
import com.tonyk.android.rickandmorty.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    private var _binding: FragmentLocationDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: LocationDetailsFragmentArgs by navArgs()
    private val locationDetailsViewmodel: DetailsViewModel by viewModels()
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

        setupUi()
        setupListData(args.location.residents)
        setupAdapter()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupUi() {
        binding.apply {
            locationName.text = args.location.name
            locationtypeTxt.text = args.location.type
            dimensionTxt.text = args.location.dimension
            SwipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    private fun setupListData(urls : List<String>) {
        val idList = mutableListOf<String>()
        for (url in urls) {
            val id = url.substringAfterLast("/")
            if (id!="") idList.add(id)
        }
        if (idList.size < 1)  { binding.progressBar.isVisible = false
            binding.emptyStateText.isVisible = true }
        else {
            locationDetailsViewmodel.getStatus(NetworkChecker.isNetworkAvailable(requireContext()), idList) }
    }
    private fun setupAdapter() {
        val adapter = CharactersListAdapter(
            onCharacterClicked = { it ->
                findNavController().navigate(LocationDetailsFragmentDirections.toCharacterDetails(it))
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
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    emptyStateText.isVisible = loadStates.refresh is LoadState.Error
                }
            }
        }
    }

    private fun refreshData() {
        val statusRefreshed = NetworkChecker.isNetworkAvailable(requireContext())
        locationDetailsViewmodel.refreshPage(statusRefreshed)
        binding.SwipeRefreshLayout.isRefreshing = false
    }
}