package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tonyk.android.rickandmorty.util.NetworkChecker
import com.tonyk.android.rickandmorty.viewmodel.CharacterDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {
    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val detVM: CharacterDetailsViewModel by viewModels()

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

        val urls = args.character.episode
        val ld = mutableListOf<String>()
        for (url in urls) {
            val parts = url.split('/')
            val lastDigit = parts.last()
            ld.add(lastDigit)
        }

        binding.charDetailsName.text = args.character.name
        binding.characterPhoto.load(args.character.image)
        binding.charLocationName.text = args.character.location.name


        detVM.getStatus(NetworkChecker.isNetworkAvailable(requireContext()), ld)

        binding.charEpisodesList.layoutManager = LinearLayoutManager(context)

        val adapter = EpListCharAdapter(
            onEpisodeClicked = {
                findNavController().navigate(
                    CharacterDetailsFragmentDirections.toEpisodeDetailsFragment(
                        it
                    )
                )
            }
        )

        binding.charEpisodesList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detVM.episodes.collect() { data ->
                    adapter.submitList(data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}