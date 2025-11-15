package com.example.ecommerceapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecommerceapp.data.models.Product

@Composable
fun ProductScreen(
    products: List<Product>,
    onAddToCart: (Product) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AsyncImage(
                        model = product.images[0],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                                .height(150.dp)
                            .fillMaxWidth()
                    )
                    Text(product.title, style = MaterialTheme.typography.titleLarge)
                    Text(product.price.toString())
                    Button(
                        onClick = { onAddToCart(product) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Add to cart")
                    }
                }
            }
        }
    }
}
