package com.example.cocktailbar.cocktaillist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailbar.db.Cocktail
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val repository = ListRepository()
    private val handler = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    private var _list = MutableLiveData<List<Cocktail>>()
    val list: MutableLiveData<List<Cocktail>>
        get() = _list

    fun getList() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _list.postValue(repository.getList())
        }
    }
}