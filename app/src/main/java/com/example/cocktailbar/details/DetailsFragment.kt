package com.example.cocktailbar.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.cocktailbar.R
import com.example.cocktailbar.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val binding: FragmentDetailsBinding by viewBinding()
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCocktail(args.id)
        observeItem()
        binding.detailsEditButton.setOnClickListener {
            val action =
                DetailsFragmentDirections.actionDetailsFragmentToEditFragment(args.id.toString())
            findNavController().navigate(action)
        }
    }

    private fun observeItem() {
        viewModel.item.observe(viewLifecycleOwner) {
            binding.detailsCoctailTitle.text = it.title
            binding.detailsDescription.text = it.description ?: ""
            binding.detailsRecipe.text = it.recipe ?: ""
            Glide.with(requireContext()).load(it.image).placeholder(R.drawable.image_photo)
                .into(binding.detailsBackground)
        }
    }
}