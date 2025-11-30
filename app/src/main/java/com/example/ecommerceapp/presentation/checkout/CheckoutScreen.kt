package com.example.ecommerceapp.presentation.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.data.viewmodels.CheckoutViewModel

val DeepNavy = Color(0xFF1B1B30)
val CardNavy = Color(0xFF25253E)
val HighlightPurple = Color(0xFF5D5FEF)
val IconBgPurple = Color(0xFF5D5FEF).copy(alpha = 0.2f)
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFF8F90A6)
val GoldYellow = Color(0xFFFFB800)
val TealGreen = Color(0xFF00C48C)
val SelectionBg = Color(0xFF32324A)

data class UserBillingData(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = ""
)

data class ShippingMethod(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: Double,
    val color: Color
)


@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel = viewModel(),
    navController: NavController,
    totalAmount: String
) {
    val userData by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedShippingId by remember { mutableStateOf("fast") }

    val shippingMethods = listOf(
        ShippingMethod("fast", "Fast Shipping", "1 days delivery for $1.0", 1.0, GoldYellow),
        ShippingMethod("regular", "Regular", "3-7 days delivery for $0.4", 0.4, TealGreen),
        ShippingMethod("courier", "Courier", "5-8 days delivery for $0.3", 0.3, TealGreen)
    )

    val selectedMethod = shippingMethods.find { it.id == selectedShippingId }
    val shippingPrice = selectedMethod?.price ?: 0
    val totalPrice = totalAmount.toDouble() + shippingPrice.toDouble()

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            TopAppBar(userData.fullName, navController)
        },
        bottomBar = {
            BottomCheckoutBar(totalPrice = totalPrice, navController)
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = HighlightPurple)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BillingInfoCard(userData, navController)

                ShippingMethodCard(
                    methods = shippingMethods,
                    selectedId = selectedShippingId,
                    onSelect = { selectedShippingId = it })

            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String, navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Billing Information",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = DeepNavy
        )
    )
}

@Composable
fun BillingInfoCard(data: UserBillingData, navController: NavController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Billing Information",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

            InfoRow(icon = Icons.Default.Person, label = "Full Name", value = data.fullName)
            InfoRow(icon = Icons.Default.Email, label = "Email Address", value = data.email)
            InfoRow(icon = Icons.Default.Phone, label = "Phone", value = data.phone)
            InfoRow(icon = Icons.Default.LocalShipping, label = "Shipping:", value = data.address)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("edit_profile") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HighlightPurple)
            ) {
                Text(text = "Edit Billing Information", color = TextWhite, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(IconBgPurple, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = HighlightPurple,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = TextWhite, fontSize = 14.sp)
        }
        Text(
            text = value,
            color = TextGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ShippingMethodCard(
    methods: List<ShippingMethod>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardNavy),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Shipping Method",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            methods.forEach { method ->
                ShippingOptionRow(
                    method = method,
                    isSelected = method.id == selectedId,
                    onSelect = { onSelect(method.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ShippingOptionRow(
    method: ShippingMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = if (isSelected) SelectionBg else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onSelect() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Custom Radio Button Look
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = method.color,
                unselectedColor = method.color
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = method.title,
            color = TextWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = method.subtitle,
            color = TextGray,
            fontSize = 13.sp
        )
    }
}

@Composable
fun BottomCheckoutBar(totalPrice: Double, navController: NavController) {
    Surface(
        color = CardNavy,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$${totalPrice}",
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { navController.navigate("payment") },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HighlightPurple),
                modifier = Modifier.height(50.dp)
            ) {
                Text(
                    text = "Confirm & Pay",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun CheckoutScreenPreview() {
//    CheckOutScreen()
}