package com.example.ecommerceapp.presentation.viewmodels

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.TabletAndroid
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.Remote.RetrofitInstance
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.data.models.User
import com.example.ecommerceapp.data.repo.FavoritesRepository
import com.example.ecommerceapp.data.repo.ProductRepository
import com.example.ecommerceapp.presentation.home.Category
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    private val targetCategories = listOf(
        "beauty", "furniture", "groceries", "mens-shirts",
        "tablets", "vehicle", "smartphones", "sunglasses"
    )
    private val _relatedProducts = MutableStateFlow<List<Product>>(emptyList())
    val relatedProducts: StateFlow<List<Product>> = _relatedProducts

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private var currentCategoryFilter: String? = null
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _endReached = MutableStateFlow(false)

    private var currentSkip = 0
    private val pageSize = 10


    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    enum class PasswordChangeResult {
        SUCCESS,
        INVALID_CREDENTIAL,
        REQUIRES_RECENT_LOGIN,
        GENERIC_ERROR
    }

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()


    init {
        fetchProducts()
        fetchCategories()
        loadProducts(category = null)
    }



    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            val currentFavorites = _favoriteIds.value.toMutableSet()
            val favoritesRepository = FavoritesRepository()
            if (currentFavorites.contains(product.id)) {
                favoritesRepository.removeFavorite(product.id)
                currentFavorites.remove(product.id)
            } else {
                favoritesRepository.addFavorite(product)
                currentFavorites.add(product.id)
            }

            _favoriteIds.value = currentFavorites
        }
    }

    fun fetchFavorites() {
        val favoritesRepository = FavoritesRepository()

        viewModelScope.launch {
            val favorites = favoritesRepository.fetchFavoriteIds()
            _favoriteIds.value = favorites
            Log.d("FavoritesViewModel", "Favorites fetched: ${_favoriteIds.value}")
            Log.d("FavoritesViewModel", "Favorites fetched: ${favoriteIds.value}")
        }
    }

    fun loadProducts(category: String? = null) {
        if (category != currentCategoryFilter) {
            _products.value = emptyList()
            currentSkip = 0
            _endReached.value = false
            currentCategoryFilter = category
        }

        fetchProducts()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val apiCategories = RetrofitInstance.api.getCategoryList()

                val filteredStrings = apiCategories.filter { it in targetCategories }

                val mappedCategories = filteredStrings.map { name ->
                    Category(
                        title = name.replace("-", " ").capitalizeWords(),
                        icon = getIconForCategory(name),
                        isHighlight = name == "groceries"
                    )
                }

                _categories.value = mappedCategories

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun getIconForCategory(category: String): androidx.compose.ui.graphics.vector.ImageVector {
        return when (category) {
            "beauty" -> Icons.Default.Face
            "furniture" -> Icons.Default.Chair // or Weekend
            "groceries" -> Icons.Default.LocalGroceryStore
            "mens-shirts" -> Icons.Default.Checkroom
            "tablets" -> Icons.Default.TabletAndroid
            "vehicle" -> Icons.Default.DirectionsCar
            "smartphones" -> Icons.Default.Smartphone
            "sunglasses" -> Icons.Default.RemoveRedEye
            else -> Icons.Default.Category
        }
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") {
        it.replaceFirstChar { char -> char.uppercase() }
    }


    fun fetchProducts() {
        if (_isLoading.value || _endReached.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = if (currentCategoryFilter.isNullOrBlank()) {
                    RetrofitInstance.api.getProducts(limit = pageSize, skip = currentSkip)
                } else {
                    RetrofitInstance.api.getProductsByCategory(
                        category = currentCategoryFilter!!,
                        limit = pageSize,
                        skip = currentSkip
                    )
                }

                val newProducts = response.products

                if (newProducts.isEmpty()) {
                    _endReached.value = true
                } else {
                    _products.value += newProducts
                    currentSkip += pageSize
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun fetchRelatedProducts(category: String, currentProductId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getProductsByCategory(
                    category = category,
                    limit = 10,
                    skip = 0
                )

                val filteredList = response.products.filter { it.id != currentProductId }

                _relatedProducts.value = filteredList.take(3)

            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching related products: ${e.message}")
                e.printStackTrace()
                _relatedProducts.value = emptyList()
            }
        }
    }



}

