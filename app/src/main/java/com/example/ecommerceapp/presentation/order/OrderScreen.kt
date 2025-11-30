package com.example.ecommerceapp.presentation.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecommerceapp.data.models.Order
import com.example.ecommerceapp.data.models.OrderStatus
import com.example.ecommerceapp.data.viewmodels.OrderViewModel
import com.example.ecommerceapp.presentation.profile.DarkBlueBackground
import com.example.ecommerceapp.presentation.profile.DarkerInputBackground
import com.example.ecommerceapp.presentation.profile.GrayText
import com.example.ecommerceapp.presentation.profile.HeaderBackground
import com.example.ecommerceapp.presentation.profile.WhiteText
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrderViewModel = viewModel()
) {
    // Collect the list of orders and loading state
    val orders by viewModel.orders.collectAsState()
    val isOrdersLoading by viewModel.isOrdersLoading.collectAsState()

    // Trigger data fetch when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.fetchOrders()
    }

    Scaffold(
        containerColor = DarkBlueBackground,
        topBar = {
            OrdersAppBar(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isOrdersLoading -> {
                    // Loading state
                    CircularProgressIndicator(
                        color = WhiteText,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                orders.isEmpty() -> {
                    // Empty state
                    Text(
                        text = "No previous orders found.",
                        color = GrayText,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    // Data loaded state
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(orders) { order ->
                            OrderCard(order = order)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkerInputBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Order ID and Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order #${order.fireId?.substring(0, 8)?.uppercase()}",
                    color = WhiteText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = dateFormatter.format(order.orderDate),
                    color = GrayText,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = HeaderBackground.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            OrderCardRow("Payment Method", order.paymentMethod.toString(), WhiteText)
            Spacer(modifier = Modifier.height(4.dp))
            OrderCardRow("Time", timeFormatter.format(order.orderDate), GrayText)
            Spacer(modifier = Modifier.height(4.dp))
            OrderCardRow("Status", order.status.toString(), getStatusColor(order.status))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Shipping To:",
                color = GrayText,
                fontSize = 14.sp,
            )
            order.deliveryAddress?.let {
                Text(
                    text = it,
                    color = WhiteText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Removed Total Amount as it is not in the new model

        }
    }
}

@Composable
fun OrderCardRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = GrayText, fontSize = 14.sp)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
// Function now accepts OrderStatus enum
fun getStatusColor(status: OrderStatus): Color {
    return when (status) {
        OrderStatus.DELIVERED -> Color(0xFF66BB6A) // Green
        OrderStatus.SHIPPED -> Color(0xFF42A5F5) // Blue
        OrderStatus.PROCESSING -> Color(0xFFFFCA28) // Yellow
        OrderStatus.CANCELLED -> Color(0xFFEF5350) // Red
        OrderStatus.PENDING -> GrayText
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                "Order History",
                color = WhiteText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = WhiteText
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Open menu or settings */ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = WhiteText
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = HeaderBackground)
    )
}