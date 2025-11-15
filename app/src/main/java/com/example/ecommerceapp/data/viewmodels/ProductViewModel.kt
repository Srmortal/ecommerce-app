package com.example.ecommerceapp.data.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.models.Product
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductViewModel: ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _searchResults = MutableLiveData<List<Product>>()
    val searchResults: LiveData<List<Product>> = _searchResults


    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchProducts(){
        viewModelScope.launch {
            try {
                val firestore = Firebase.firestore
                val snapshot = firestore.collection("products").get().await()
                val fetchedProducts = snapshot.toObjects(Product::class.java)
                _products.postValue(fetchedProducts)
            }
            catch (e: Exception){
                _products.postValue(emptyList())
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()){
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            try {
                val firestore = Firebase.firestore
                val snapshot = firestore.collection("products")
                    .orderBy("title")
                    .startAt(query)
                    .endAt(query + '\uf8ff')
                    .get()
                    .await()
                val filteredProducts = snapshot.toObjects(Product::class.java).filter {
                    it.title.contains(query, ignoreCase = true)
                }
                _searchResults.postValue(filteredProducts)
            }
            catch (e: Exception){
                _searchResults.postValue(emptyList())
            }
        }
    }
}