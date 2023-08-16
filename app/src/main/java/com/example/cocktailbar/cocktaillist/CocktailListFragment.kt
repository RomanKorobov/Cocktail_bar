package com.example.cocktailbar.cocktaillist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cocktailbar.R
import com.example.cocktailbar.autoCleared
import com.example.cocktailbar.databinding.FragmentCocktailListBinding
import com.example.cocktailbar.db.Cocktail

class CocktailListFragment : Fragment(R.layout.fragment_cocktail_list) {
    private val binding: FragmentCocktailListBinding by viewBinding()
    private val viewModel: ListViewModel by viewModels()
    private var fragmentAdapter: ListAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        observeMenuList()
        viewModel.getList()
        binding.fab.setOnClickListener {
            val action =
                CocktailListFragmentDirections.actionCocktailListFragmentToEditFragment(null, null)
            findNavController().navigate(action)
        }
    }

    private fun initList() {
        fragmentAdapter = ListAdapter { id ->
            val action =
                CocktailListFragmentDirections.actionCocktailListFragmentToDetailsFragment(id)
            findNavController().navigate(action)
        }
        binding.coctailListMainRecView.adapter = fragmentAdapter
        binding.coctailListMainRecView.layoutManager = GridLayoutManager(context, 2)
        binding.coctailListMainRecView.setHasFixedSize(true)
    }

    private fun observeMenuList() {
        viewModel.list.observe(viewLifecycleOwner) {
            val newItems = it as MutableList<Cocktail>
            fragmentAdapter.items = newItems
            fragmentAdapter.notifyDataSetChanged()
            if (newItems.isNotEmpty()) {
                binding.imageViewHead.visibility = View.GONE
                binding.myCocktailsHint.visibility = View.GONE
                binding.arrow1.visibility = View.GONE
            } else {
                binding.imageViewHead.visibility = View.VISIBLE
                binding.myCocktailsHint.visibility = View.VISIBLE
                binding.arrow1.visibility = View.VISIBLE
            }
        }
    }
}