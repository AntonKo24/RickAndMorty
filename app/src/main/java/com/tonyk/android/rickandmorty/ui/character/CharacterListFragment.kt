package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tonyk.android.rickandmorty.databinding.FragmentCharactersBinding
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterListFragment : Fragment() {
    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val charactersViewModel: CharactersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val status = NetworkChecker.isNetworkAvailable(requireContext())
        charactersViewModel.getStatus(status)

        val adapter = CharactersListAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                charactersViewModel.characters.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
        binding.charactersRcv.layoutManager = LinearLayoutManager(context)
        binding.charactersRcv.adapter = adapter

        binding.characterSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    charactersViewModel.applyFilter(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                }
                return true
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}