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
import com.tonyk.android.rickandmorty.viewmodel.character.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFilterFragment : Fragment() {
    private var _binding: FragmentCharactersFilterBinding? = null
    private val binding get() = _binding!!
    private val charactersListViewModel: CharactersListViewModel by activityViewModels()

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

        val currentFilter = charactersListViewModel.getCurrentFilter()

        binding.apply {

            clearBtn.setOnClickListener {
                namePicker.text.clear()
                speciesPicker.text.clear()
                typePicker.text.clear()
                statusGroup.clearCheck()
                genderGroup.clearCheck()
            }

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            namePicker.setText(currentFilter.name)
            speciesPicker.setText(currentFilter.species)
            typePicker.setText(currentFilter.type)

            when (currentFilter.status) {
                "Alive" -> statusGroup.check(R.id.alivePick)
                "Dead" -> statusGroup.check(R.id.deadPick)
                "unknown" -> statusGroup.check(R.id.UnknowStatusPick)
            }

            when (currentFilter.gender) {
                "Male" -> genderGroup.check(R.id.malePick)
                "Female" -> genderGroup.check(R.id.femalePick)
                "Genderless" -> genderGroup.check(R.id.genderlessPick)
                "unknown" -> genderGroup.check(R.id.unknownGenderPick)
            }

            applyButton.setOnClickListener {
                val name = namePicker.text.toString().takeIf { it.isNotEmpty() }
                val species = speciesPicker.text.toString().takeIf { it.isNotEmpty() }
                val type = typePicker.text.toString().takeIf { it.isNotEmpty() }

                val selectedStatus = when (statusGroup.checkedRadioButtonId) {
                    R.id.alivePick -> "Alive"
                    R.id.deadPick -> "Dead"
                    R.id.UnknowStatusPick -> "unknown"
                    else -> null
                }
                val selectedGender = when (genderGroup.checkedRadioButtonId) {
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

                charactersListViewModel.applyFilter(characterFilter)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}