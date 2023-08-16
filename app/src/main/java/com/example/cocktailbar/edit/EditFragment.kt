package com.example.cocktailbar.edit

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cocktailbar.R
import com.example.cocktailbar.autoCleared
import com.example.cocktailbar.databinding.FragmentEditBinding
import com.example.cocktailbar.db.Cocktail
import kotlin.random.Random

class EditFragment : Fragment(R.layout.fragment_edit) {
    private val binding: FragmentEditBinding by viewBinding()
    private val viewModel: EditViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()
    private var fragmentAdapter: IngredientsAdapter by autoCleared()
    private var currentId: Int? = null
    private var selectedImageUri: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs: SharedPreferences =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        initList()
        observeItem()
        val id = args.id?.toIntOrNull()
        if (id != null) {
            if (args.newIngredient == null) {
                viewModel.getItem(id)
            } else {
                val savedList = sharedPrefs.getString(
                    INGREDIENTS_FRAGMENT_LIST, fragmentAdapter.ingredients.joinToString("$")
                )?.split("$") as MutableList<String>
                if (args.newIngredient != null) {
                    fragmentAdapter.ingredients =
                        (savedList + mutableListOf(args.newIngredient ?: "")) as MutableList
                } else fragmentAdapter.ingredients = savedList
                fragmentAdapter.clearEmpty()
                fragmentAdapter.notifyDataSetChanged()
                selectedImageUri = sharedPrefs.getString(INGREDIENTS_IMAGE_URI, "")
                Glide.with(requireContext()).load(selectedImageUri)
                    .placeholder(R.drawable.image_photo)
                    .apply(RequestOptions().centerCrop())
                    .into(binding.imageView)
            }
        } else {
            val savedList = sharedPrefs.getString(
                INGREDIENTS_FRAGMENT_LIST, fragmentAdapter.ingredients.joinToString("$")
            )?.split("$") as MutableList<String>
            if (args.newIngredient != null) {
                fragmentAdapter.ingredients =
                    (savedList + mutableListOf(args.newIngredient ?: "")) as MutableList
            } else fragmentAdapter.ingredients = savedList
            fragmentAdapter.clearEmpty()
            fragmentAdapter.notifyDataSetChanged()
            selectedImageUri = sharedPrefs.getString(INGREDIENTS_IMAGE_URI, "")
            Glide.with(requireContext()).load(selectedImageUri).placeholder(R.drawable.image_photo)
                .apply(RequestOptions().centerCrop())
                .into(binding.imageView)
        }
        binding.coctailNameEditText.setText(sharedPrefs.getString(INGREDIENTS_TITLE, ""))
        binding.descriptionEditText.setText(sharedPrefs.getString(INGREDIENTS_DESCRIPTION, ""))
        binding.recipeEditText.setText(sharedPrefs.getString(INGREDIENTS_RECIPE, ""))
        val currentTitle = binding.coctailNameEditText
        val currentDescription = binding.descriptionEditText
        val currentRecipe = binding.recipeEditText

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                putSharedPrefs(
                    currentTitle.text.toString(),
                    currentDescription.text.toString(),
                    currentRecipe.text.toString()
                )
            }
        }

        binding.coctailNameEditText.addTextChangedListener(watcher)
        binding.descriptionEditText.addTextChangedListener(watcher)
        binding.recipeEditText.addTextChangedListener(watcher)

        binding.saveButton.setOnClickListener {
            fragmentAdapter.clearEmpty()
            add(fragmentAdapter.ingredients.joinToString("$"))
            clearSharedPrefs()
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToCocktailListFragment())
        }
        binding.cancelButton.setOnClickListener {
            clearSharedPrefs()
            findNavController().popBackStack()
        }

        binding.photoCardview.setOnClickListener {
            val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickImage, PICK_IMAGE_REQUEST)
        }

        binding.addIngredientButton.setOnClickListener {
            sharedPrefs.edit()
                .putString(INGREDIENTS_FRAGMENT_LIST, fragmentAdapter.ingredients.joinToString("$"))
                .apply()
            val action = EditFragmentDirections.actionEditFragmentToDialog(currentId.toString())
            findNavController().navigate(action)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data.toString()
            requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit().putString(
                    INGREDIENTS_IMAGE_URI, selectedImageUri
                ).apply()
            Glide.with(requireContext()).load(selectedImageUri).apply(RequestOptions().centerCrop())
                .into(binding.imageView)
        }
    }

    private fun observeItem() {
        val sharePrefs =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        viewModel.item.observe(viewLifecycleOwner) {
            binding.coctailNameEditText.setText(it.title)
            binding.descriptionEditText.setText(it.description ?: "")
            binding.recipeEditText.setText(it.recipe ?: "")
            fragmentAdapter.ingredients = it.ingredients?.split("$") as MutableList<String>
            if (args.newIngredient != null) {
                val new = (requireContext().getSharedPreferences(
                    SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE
                ).getString(
                    INGREDIENTS_FRAGMENT_LIST, fragmentAdapter.ingredients.joinToString("$")
                )?.split("$")
                    ?.plus(mutableListOf(args.newIngredient ?: ""))) as MutableList
                fragmentAdapter.ingredients = new
            }
            fragmentAdapter.notifyDataSetChanged()
            currentId = it.id
            selectedImageUri = it.image
            sharePrefs.edit()
                .putString(INGREDIENTS_FRAGMENT_LIST, fragmentAdapter.ingredients.joinToString("$"))
                .apply()
            sharePrefs.edit().putString(INGREDIENTS_IMAGE_URI, selectedImageUri).apply()
            Glide.with(requireContext()).load(selectedImageUri).apply(RequestOptions().centerCrop())
                .into(binding.imageView)
        }
    }

    private fun initList() {
        fragmentAdapter = IngredientsAdapter {
            try {
                val new = fragmentAdapter.ingredients
                new.removeAt(it)
                fragmentAdapter.ingredients = new
                fragmentAdapter.notifyDataSetChanged()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        binding.ingredientsRecView.adapter = fragmentAdapter
        binding.ingredientsRecView.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun add(ingredients: String) {
        val title = binding.coctailNameEditText.text.toString() ?: ""
        val description = binding.descriptionEditText.text.toString()
        val recipe = binding.recipeEditText.text.toString()
        val image = selectedImageUri
        val cocktail =
            Cocktail(currentId ?: Random.nextInt(), title, description, recipe, image, ingredients)
        viewModel.addItem(cocktail)
    }

    private fun clearSharedPrefs() {
        val prefs =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(INGREDIENTS_FRAGMENT_LIST).apply()
        prefs.edit().remove(INGREDIENTS_IMAGE_URI).apply()
        prefs.edit().remove(INGREDIENTS_RECIPE).apply()
        prefs.edit().remove(INGREDIENTS_TITLE).apply()
        prefs.edit().remove(INGREDIENTS_DESCRIPTION).apply()
    }

    private fun putSharedPrefs(title: String, description: String?, recipe: String?) {
        val prefs =
            requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(INGREDIENTS_TITLE, title).apply()
        prefs.edit().putString(INGREDIENTS_DESCRIPTION, description).apply()
        prefs.edit().putString(INGREDIENTS_RECIPE, recipe).apply()
        prefs.edit()
            .putString(INGREDIENTS_FRAGMENT_LIST, fragmentAdapter.ingredients.joinToString("$"))
            .apply()
        prefs.edit().putString(INGREDIENTS_IMAGE_URI, selectedImageUri).apply()
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 11001100
        const val INGREDIENTS_FRAGMENT_LIST = "INGREDIENTS FRAGMENT LIST"
        const val INGREDIENTS_IMAGE_URI = "INGREDIENTS IMAGE URI"
        const val INGREDIENTS_TITLE = "INGREDIENTS TITLE"
        const val INGREDIENTS_DESCRIPTION = "INGREDIENTS DESCRIPTION"
        const val INGREDIENTS_RECIPE = "INGREDIENTS RECIPE"
        private const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
    }
}