package com.example.ecommerceapp.presentation.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.local.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val repository: DataStoreRepository) : ViewModel() {

    // Save the onboarding state (completed or not)
    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOnBoardingState(completed)
        }
    }

    // Check if onboarding is completed and return the state as a Flow
    fun isOnBoardingCompleted(): Flow<Boolean> {
        return repository.readOnBoardingState()
    }
}