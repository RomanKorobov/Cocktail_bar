package com.example.cocktailbar.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailbar.db.Cocktail
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel : ViewModel() {
    private val repository = EditRepository()
    private val handler = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    private var _theItem = MutableLiveData<Cocktail>()
    val item: MutableLiveData<Cocktail>
        get() = _theItem

    fun addItem(cocktail: Cocktail) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.addItem(cocktail)
        }
    }

    fun getItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _theItem.postValue(repository.getItem(id))
        }
    }
}