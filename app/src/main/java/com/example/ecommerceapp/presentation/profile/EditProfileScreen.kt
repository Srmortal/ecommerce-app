//package com.example.ecommerceapp.presentation.editprofile
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
//import androidx.compose.material3.*
//import androidx.compose.ui.text.input.TextFieldValue
//
//@Composable
//fun EditProfileScreen() {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = Color(0xFF2A2C3E)
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
////            UserInputField(label = "Username", value = "designing-world")
////            UserInputField(label = "Full Name", value = "Suha Jannat")
////            UserInputField(label = "Phone", value = "+880 000 111 222")
////            UserInputField(label = "Email Address", value = "care@example.com")
////            UserInputField(label = "Shipping Address", value = "28/C Green Road, BD")
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = { /* Handle Save Changes */ },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF776DFF)),
//                shape = RoundedCornerShape(8.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp)
//            ) {
//                Text(text = "Save All Changes", color = Color.White, fontSize = 16.sp)
//            }
//        }
//    }
//}
////@Composable
////fun UserInputField(label: String, value: String) {
////    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
////        Text(text = label, color = Color.White)
////        TextField(
////            value = TextFieldValue(value),
////            onValueChange = {},
////            modifier = Modifier
////                .fillMaxWidth()
////                .background(Color(0xFF3A3C47), shape = RoundedCornerShape(8.dp)),
////            colors = TextFieldDefaults.textFieldColors(
////                textColor = Color.White,
////                placeholderColor = Color.Gray,
////                containerColor = Color.Transparent,
////                focusedIndicatorColor = Color.Transparent,
////                unfocusedIndicatorColor = Color.Transparent
////            )
////        )
////    }
////}
//
//@Preview(showBackground = false, showSystemUi = true)
//@Composable
//fun PreviewEditProfileScreen(){
//    EcommerceAppTheme {
//        EditProfileScreen()
//    }
//}