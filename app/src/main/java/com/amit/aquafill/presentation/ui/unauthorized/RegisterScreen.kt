package com.amit.aquafill.presentation.ui.unauthorized

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Register(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(
            top = 20.dp,
            bottom = 20.dp,
            start = 65.dp,
            end = 65.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "REGISTER", fontSize = 30.sp)
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var repeatPassword by remember { mutableStateOf(TextFieldValue("")) }
        var name by remember { mutableStateOf(TextFieldValue("")) }
        var phone by remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text(text = "Name") },
            placeholder = { Text(text = "Enter your name") },
        )
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            label = { Text(text = "Phone") },
            placeholder = { Text(text = "Enter your phone no") },
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = "Email") },
            placeholder = { Text(text = "Enter your email") },
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "Password") },
            placeholder = { Text(text = "Enter your password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = {
                repeatPassword = it
            },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "Repeat Password") },
            placeholder = { Text(text = "Enter password again") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(onClick = { /*TODO*/ },
            modifier = Modifier.padding(
                top = 20.dp
            )) {
            Text(
                "Register", modifier = Modifier.padding(
                    start = 30.dp,
                    end = 30.dp,
                )
            )
        }
        ClickableText(text = AnnotatedString("Already have an account? Sign in"),
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(
                top = 20.dp
            ),
            style = TextStyle(color = Color.Blue)
        )
    }
}
