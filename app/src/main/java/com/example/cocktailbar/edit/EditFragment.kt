package com.example.cocktailbar.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cocktailbar.R
import com.example.cocktailbar.databinding.FragmentEditBinding
import com.example.cocktailbar.db.Cocktail
import kotlin.random.Random

class EditFragment : Fragment(R.layout.fragment_edit) {
    private val binding: FragmentEditBinding by viewBinding()
    private val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.id != null) {
            observeItem()
            val id = args.id!!.toIntOrNull()
            if (id != null) {
                viewModel.getItem(id)
            }
        }
        binding.saveButton.setOnClickListener {
            val title = binding.coctailNameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val recipe = binding.recipeEditText.text.toString()
            val ingredients = ""
            val image = ""
            val cocktail =
                Cocktail(Random.nextInt(), title, description, recipe, image, ingredients)
            viewModel.addItem(cocktail)
            findNavController().popBackStack()
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeItem() {
        viewModel.item.observe(viewLifecycleOwner) {
            binding.coctailNameEditText.setText(it.title)
            binding.descriptionEditText.setText(it.description ?: "")
            binding.recipeEditText.setText(it.recipe ?: "")
        }
    }
}