package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.FragmentCharactersFilterBinding
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.viewmodel.CharactersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFilterFragment : Fragment() {
    private var _binding: FragmentCharactersFilterBinding? = null
    private val binding get() = _binding!!
    private val charactersViewModel: CharactersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharactersFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentFilter = charactersViewModel.getCurrentFilter()

        binding.clearBtn.setOnClickListener {

            binding.namePicker.text.clear()
            binding.speciesPicker.text.clear()
            binding.typePicker.text.clear()
            binding.statusGroup.clearCheck()
            binding.genderGroup.clearCheck()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.namePicker.setText(currentFilter.name)
        binding.speciesPicker.setText(currentFilter.species)
        binding.typePicker.setText(currentFilter.type)

        when (currentFilter.status) {
            "Alive" -> binding.statusGroup.check(R.id.alivePick)
            "Dead" -> binding.statusGroup.check(R.id.deadPick)
            "unknown" -> binding.statusGroup.check(R.id.UnknowStatusPick)
        }

        when (currentFilter.gender) {
            "Male" -> binding.genderGroup.check(R.id.malePick)
            "Female" -> binding.genderGroup.check(R.id.femalePick)
            "Genderless" -> binding.genderGroup.check(R.id.genderlessPick)
            "unknown" -> binding.genderGroup.check(R.id.unknownGenderPick)
        }

        binding.applyButton.setOnClickListener {
            val name = binding.namePicker.text.toString().takeIf { it.isNotEmpty() }
            val species = binding.speciesPicker.text.toString().takeIf { it.isNotEmpty() }
            val type = binding.typePicker.text.toString().takeIf { it.isNotEmpty() }

            val selectedStatus = when (binding.statusGroup.checkedRadioButtonId) {
                R.id.alivePick -> "Alive"
                R.id.deadPick -> "Dead"
                R.id.UnknowStatusPick -> "unknown"
                else -> null
            }
            val selectedGender = when (binding.genderGroup.checkedRadioButtonId) {
                R.id.malePick -> "Male"
                R.id.femalePick -> "Female"
                R.id.genderlessPick -> "Genderless"
                R.id.unknownGenderPick -> "unknown"
                else -> null
            }

            val characterFilter = CharacterFilter(
                name = name,
                species = species,
                type = type,
                status = selectedStatus,
                gender = selectedGender
            )

            charactersViewModel.applyFilter(characterFilter)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}