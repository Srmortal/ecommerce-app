package com.example.ecommerceapp.presentation.payment

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.data.models.PaymentMethod
import com.example.ecommerceapp.presentation.viewmodels.OrderViewModel
import com.example.ecommerceapp.presentation.profile.HeaderBackground
import com.example.ecommerceapp.presentation.profile.LightBlue
import com.google.firebase.auth.FirebaseAuth


private val CardBackground = Color(0xFF242A50)
private val IconTint = Color(0xFFC7B1E4)
private val CheckIconColor = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePaymentMethodScreen(
    navController: NavController,
    viewModel: OrderViewModel = viewModel()
) {

    val userId by viewModel.userId.collectAsState()
    val deliveryAddress = navController.previousBackStackEntry?.arguments?.getString("address")
    var selectedPaymentMethod by remember { mutableStateOf("Cash On Delivery") }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Choose Payment Method",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HeaderBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1B3C))
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            PaymentMethodCard(
                icon = Icons.Default.ShoppingCart,
                label = "Cash On Delivery",
                isSelected = selectedPaymentMethod == "Cash On Delivery",
                onClick = { selectedPaymentMethod = "Cash On Delivery" },
                iconColor = IconTint
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    Log.d("PaymentMethod", "data ${userId} ${deliveryAddress}")
                    val paymentMethodEnum = when (selectedPaymentMethod) {
                        "Cash On Delivery" -> PaymentMethod.CASH_ON_DELIVERY
                        else -> PaymentMethod.CASH_ON_DELIVERY
                    }
                    viewModel.setOrderDetails(
                        deliveryAddress = deliveryAddress?: "Delivery Address"
                    )
                    navController.navigate("payment/${deliveryAddress?: "Delivery Address"}")
                    Log.d(
                        "PaymentMethod",
                        "data ${userId} ${paymentMethodEnum} ${deliveryAddress}"
                    )
                    viewModel.confirmOrder(
                        userId = userId,
                        paymentMethod = paymentMethodEnum,
                        deliveryAddress = deliveryAddress,
                        onFailure = { e ->
                            Toast.makeText(context, "Order failed: ${e.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
            ) {
                Text(
                    "Confirm Payment",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun PaymentMethodCard(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    iconColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier
            .size(160.dp)
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = if (isSelected) LightBlue else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = CheckIconColor,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}