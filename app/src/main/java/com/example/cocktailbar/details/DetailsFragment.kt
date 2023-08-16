package com.example.cocktailbar.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.cocktailbar.R
import com.example.cocktailbar.autoCleared
import com.example.cocktailbar.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val binding: FragmentDetailsBinding by viewBinding()
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private var fragmentAdapter: DetailsAdapter by autoCleared()
    private var currentId: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        viewModel.getCocktail(args.id)
        observeItem()

        binding.detailsEditButton.setOnClickListener {
            val action =
                DetailsFragmentDirections.actionDetailsFragmentToEditFragment(
                    args.id.toString(),
                    null
                )
            findNavController().navigate(action)
        }

        binding.detailsDeleteButton.setOnClickListener {
            currentId?.let {
                viewModel.deleteCocktail(it)
                findNavController().popBackStack()
            }
        }
    }

    private fun initList() {
        fragmentAdapter = DetailsAdapter()
        with(binding.detailsCocktailRecView) {
            adapter = fragmentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeItem() {
        viewModel.item.observe(viewLifecycleOwner) {
            binding.detailsCoctailTitle.text = it.title
            binding.detailsDescription.text = it.description ?: ""
            binding.detailsRecipe.text = it.recipe ?: ""
            val new = it.ingredients?.split("$") as MutableList<String>
            fragmentAdapter.itemsList = new
            fragmentAdapter.notifyDataSetChanged()
            currentId = it.id
            Glide.with(requireContext()).load(it.image).placeholder(R.drawable.image_photo)
                .into(binding.detailsBackground)
        }
    }
}