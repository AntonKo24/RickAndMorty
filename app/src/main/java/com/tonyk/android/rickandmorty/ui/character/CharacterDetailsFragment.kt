package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.tonyk.android.rickandmorty.databinding.FragmentCharacterDetailsBinding
import com.tonyk.android.rickandmorty.ui.episode.EpisodeListAdapter
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.CharacterDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {
    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val ld = mutableListOf<String>()
        for (url in args.character.episode) {
            val id = url.substringAfterLast("/")
            ld.add(id)
        }
        binding.apply {
            charDetailsName.text = args.character.name
            characterPhoto.load(args.character.image)
            charLocationName.text = args.character.location.name
            originLocationName.text = args.character.origin.name

            setOnClickListenerForLocation(charLocationName, args.character.location.url)
            setOnClickListenerForLocation(originLocationName, args.character.origin.url)

            characterDetailsViewModel.getStatus(
                NetworkChecker.isNetworkAvailable(requireContext()),
                ld
            )

            charEpisodesList.layoutManager = LinearLayoutManager(context)

            val adapter = EpisodeListAdapter(
                onEpisodeClicked = {
                    findNavController().navigate(
                        CharacterDetailsFragmentDirections.toEpisodeDetailsFragment(
                            it
                        )
                    )
                }
            )

            charEpisodesList.adapter = adapter
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    characterDetailsViewModel.episodes.collect() { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickListenerForLocation(textView: TextView, locationUrl: String) {
        if (locationUrl.isNotEmpty()) {
            val locationId = locationUrl.substringAfterLast("/")
            textView.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val location = withContext(Dispatchers.IO) {
                            characterDetailsViewModel.loadLocation(locationId)
                        }
                        Log.d("DebugLoc", "$location")
                        withContext(Dispatchers.Main) {
                        findNavController().navigate(
                            CharacterDetailsFragmentDirections.toLocationDetails(location)
                        )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "$e", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}