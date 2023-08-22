package com.denicks21.languageselector.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denicks21.languageselector.repository.DataStoreRepository
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _language = MutableLiveData(1)
    var language: LiveData<Int> = _language

    init {
        viewModelScope.launch {
            dataStoreRepository.getLanguage.collect {
                _language.value = it
            }
        }
    }

    suspend fun saveLanguage(language: Int) {
        dataStoreRepository.setLanguage(language)
    }
}