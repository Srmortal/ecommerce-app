package com.example.ecommerceapp.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme

@Composable
fun ProfileScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF2A2C3E)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserInfoRow(iconRes = R.drawable.at, label = "Username", value = "@designing-world")
            UserInfoRow(iconRes = R.drawable.profile, label = "Full Name", value = "SUHA JANNAT")
            UserInfoRow(iconRes = R.drawable.baseline_phone_24, label = "Phone", value = "+880 000 111 222")
            UserInfoRow(iconRes = R.drawable.baseline_email_24, label = "Email", value = "care@example.com")
            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF776DFF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(48.dp)
            ) {
                Text(text = "Edit Profile", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
@Composable
fun UserInfoRow(iconRes: Int, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label: ", color = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, color = Color.White)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    EcommerceAppTheme {
        ProfileScreen()
    }
}