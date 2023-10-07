package com.tonyk.android.rickandmorty.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tonyk.android.rickandmorty.R
import com.tonyk.android.rickandmorty.databinding.FragmentCharactersFilterBinding
import com.tonyk.android.rickandmorty.model.character.CharacterEntity
import com.tonyk.android.rickandmorty.model.character.CharacterFilter
import com.tonyk.android.rickandmorty.ui.base.BaseFragment
import com.tonyk.android.rickandmorty.viewmodel.character.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFilterFragment : BaseFragment<CharacterEntity>() {
    private var _binding: FragmentCharactersFilterBinding? = null
    private val binding get() = _binding!!

    override val viewModel: CharactersListViewModel by activityViewModels()

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

        setupUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
        val currentFilter = viewModel.getCurrentFilter()

        binding.apply {
            clearBtn.setOnClickListener {
                clearFields()
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
                applyFilter()
            }
        }
    }

    private fun clearFields() {
        binding.namePicker.text.clear()
        binding.speciesPicker.text.clear()
        binding.typePicker.text.clear()
        binding.statusGroup.clearCheck()
        binding.genderGroup.clearCheck()
    }

    private fun applyFilter() {
        val characterFilter = createCharacterFilter()
        viewModel.applyFilter(characterFilter)
        findNavController().popBackStack()
    }

    private fun createCharacterFilter(): CharacterFilter {
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

        return CharacterFilter(
            name = name,
            species = species,
            type = type,
            status = selectedStatus,
            gender = selectedGender
        )
    }
}