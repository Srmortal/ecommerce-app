import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommerceapp.SignIn
import com.example.ecommerceapp.data.models.Product
import com.example.ecommerceapp.presentation.CartScreen
import com.example.ecommerceapp.presentation.HomeScreen
import com.example.ecommerceapp.presentation.SignUp
import com.example.ecommerceapp.presentation.SingleProductScreen
import com.example.ecommerceapp.presentation.change_password.ChangePasswordScreen
import com.example.ecommerceapp.presentation.checkout.CheckoutScreen
import com.example.ecommerceapp.presentation.order.OrdersScreen
import com.example.ecommerceapp.presentation.payment.ChoosePaymentMethodScreen
import com.example.ecommerceapp.ui.theme.AppBar
import com.example.ecommerceapp.ui.theme.BottomBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.ecommerceapp.presentation.profile.EditProfileScreen
import com.example.ecommerceapp.presentation.profile.ProfileScreen
import com.example.ecommerceapp.presentation.profile.SettingsScreen
import com.example.ecommerceapp.presentation.signin.ForgetPassword
import com.example.ecommerceapp.presentation.success_order.PaymentSuccessfulScreen
import com.example.ecommerceapp.presentation.whistlist.WishlistScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    isSystemInDarkTheme()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isUserPermanentlySignedIn = currentUser != null && !currentUser.isAnonymous
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentRoute == "home" || currentRoute == "settings" || currentRoute == "wishlist" || currentRoute == "profile") {
                if (currentRoute != "wishlist") {
                    AppBar(navController)
                }
            }
        },
        bottomBar = {
            if (currentRoute == "home" || currentRoute == "settings" || currentRoute == "wishlist" || currentRoute == "profile") {
                BottomBar(navController, currentRoute)
            }
        },
    ) { innerPadding ->

        val showSnackbar: (Product) -> Unit = { product ->
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "${product.title} added to cart!",
                    actionLabel = "View Cart",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    navController.navigate("cart")
                }
            }
        }

        val startDestination = remember {
            if (isUserPermanentlySignedIn) "home" else "signin"
        }
        val showSnackbarWishlist: (String) -> Unit = { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }


        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("signup") { SignUp(navController, innerPadding) }
            composable("forget_password") { ForgetPassword(navController, innerPadding) }
            composable(
                route = "productDetails/{productId}",
                arguments = listOf(
                    navArgument("productId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->

                val productId = backStackEntry.arguments?.getInt("productId") ?: -1

                SingleProductScreen(
                    productId = productId,
                    onBackClick = { navController.popBackStack() },
                    onProductClick = { newProductId ->
                        navController.navigate("productDetails/$newProductId")
                    }
                )
            }
            composable("signup") { SignUp(navController, innerPadding) }
            composable("signin") { SignIn(navController, innerPadding) }
            composable("change_password") { ChangePasswordScreen(innerPadding, navController) }
            composable("cart") { CartScreen(navController, innerPadding) }
            composable("success_payment") { PaymentSuccessfulScreen(navController) }
            composable("edit_profile") { EditProfileScreen(navController) }
            composable("orders") { OrdersScreen(navController) }
            composable("payment") { ChoosePaymentMethodScreen(navController) }
            composable(
                route = "checkout/{totalPrice}",
                arguments = listOf(
                    navArgument("totalPrice") {
                        type = NavType.StringType
                        defaultValue = "0.00"
                    }
                )
            ) { backStackEntry ->
                val priceEncoded = backStackEntry.arguments?.getString("totalPrice") ?: "0.00"
                CheckoutScreen(totalAmount = priceEncoded, navController = navController)
            }
            composable("profile") { ProfileScreen(navController, innerPadding) }
            composable("wishlist") {
                WishlistScreen(
                    innerPadding,
                    navController,
                    showSnackbarWishlist
                )
            }
            composable("home") {
                HomeScreen(
                    onProductClick = { id ->
                        navController.navigate("productDetails/$id")
                    },
                    onProductAddedToCart = showSnackbar
                )
            }
            composable("settings") {
                SettingsScreen(navController = navController)
            }
        }
    }
}
