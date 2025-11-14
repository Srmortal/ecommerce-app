import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mms
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceapp.SignIn
import com.example.ecommerceapp.data.Constants
import com.example.ecommerceapp.data.NavItem
import com.example.ecommerceapp.presentation.Home
import com.example.ecommerceapp.presentation.SignUp
import com.example.ecommerceapp.ui.theme.AppBar
import com.example.ecommerceapp.ui.theme.BlueLogo1
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    Scaffold(
        topBar = {
            if (currentRoute == "home") {
                AppBar()
            }
        },
        bottomBar = {
            if (currentRoute == "home") {
                BottomBar(navController)
            }
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = if (currentUser != null) "home" else "signin"
        ) {
            composable("signup") { SignUp(navController, innerPadding) }
            composable("signin") { SignIn(navController, innerPadding) }
            composable("home") { Home() }
        }
    }
}


@Composable
fun BottomBar(navController: NavController) {
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

@Composable
fun AppBar() {
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
}