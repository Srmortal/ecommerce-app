package com.example.ecommerceapp.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.models.Category
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CategoryViewModel: ViewModel() {
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    fun fetchCategories(){
        viewModelScope.launch {
            try {
                // Fetch categories directly from Firestore
                val firestore = Firebase.firestore
                val snapshot = firestore.collection("categories").get().await()
                val fetchedCategories = snapshot.toObjects(Category::class.java)

                // Post the fetched list to the LiveData
                _categories.postValue(fetchedCategories) // Use postValue for background threads
                Log.d("CategoryViewModel", "Successfully fetched ${fetchedCategories.size} categories.")

            } catch (e: Exception) {
                // Handle any errors, e.g., network issues or permissions
                Log.e("CategoryViewModel", "Error fetching categories", e)
                _categories.postValue(emptyList()) // Update UI to show an empty or error state
            }
        }
    }
}