package com.example.cocktailbar.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailbar.db.Cocktail
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val repository = DetailsRepository()
    private val handler = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    private var _item = MutableLiveData<Cocktail>()
    val item: MutableLiveData<Cocktail>
        get() = _item

    fun getCocktail(id: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            item.postValue(repository.getItemById(id))
        }
    }
}