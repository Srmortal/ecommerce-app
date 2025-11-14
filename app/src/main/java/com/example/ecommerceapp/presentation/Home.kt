package com.example.ecommerceapp.presentation

import Logo
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mms
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecommerceapp.data.NavItem
import com.example.ecommerceapp.data.models.Category
import com.example.ecommerceapp.ui.theme.AppBar
import com.example.ecommerceapp.ui.theme.BlueLogo1

@Composable
fun Home(modifier: Modifier = Modifier){
    val navItems = listOf(
        NavItem(
            label = "Home",
            icon = Icons.Outlined.Home,
            onClick = {}
        ),
        NavItem(
            label = "Chat",
            icon = Icons.Outlined.Mms,
            onClick = {}
        ),
        NavItem(
            label = "Cart",
            icon = Icons.Outlined.ShoppingCart,
            onClick = {}
        ),
        NavItem(
            label = "Settings",
            icon = Icons.Outlined.Settings,
            onClick = {}
        ),
        NavItem(
            label = "Favourites",
            icon = Icons.Outlined.Favorite,
            onClick = {}
        ),
    )
    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .background(color = AppBar)
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Logo(
                    size = 48.dp,
                    mainColor = BlueLogo1,
                    secondColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBasket,
                        contentDescription = "Cart",
                    )
                    Box(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = Color.Gray,
                                shape = CircleShape
                            )
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Menu",
                                tint = Color.Gray,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(color = AppBar)
                    .fillMaxWidth()
                    .padding(2.dp),
                containerColor = AppBar
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == 0,
                        onClick = item.onClick,
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = Color.White
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                color = Color.White
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HomeScreen(modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier){
    val categories = listOf<Category>(
        Category(
            id = 1,
            name = "Electronics",
            imageUrl = "https://images.unsplash.com/photo-1761839259488-2bdeeae794f5?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            subCategoryId = null,
            fireId = "test"
        )
    )
    var searchText by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange ={
                        searchText = it
                    },
                    onSearch = {
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = it
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    placeholder = {
                        Text(text = "Search")
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            },
            modifier = Modifier.fillMaxWidth()
        ) {

        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories.size) {
                CategoryItem(category = categories[it])
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
    ){
        AsyncImage(
            model = category.imageUrl,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 60.dp, height = 40.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = category.name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomePreview(){
    Home()
}