package com.amit.aquafill.presentation.ui.unauthorized.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amit.aquafill.routes.Routes

@Composable
fun Login(navController: NavHostController) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val loginUiState by loginViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    loginViewModel.redirectIfLoggedIn(navController)

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
        Text(text = "LOGIN", fontSize = 30.sp)
        OutlinedTextField(
            value = loginUiState.currentEmail,
            onValueChange = {
                loginViewModel.updateEmail(it)
            },
            label = { Text(text = "Email address") },
            placeholder = { Text(text = "Enter your email") },
            isError = loginUiState.currentEmailErrors.isNotEmpty()
        )

        Row {
            loginUiState.currentEmailErrors.forEach {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .wrapContentWidth(Alignment.Start)
                        .fillMaxWidth(),
                    text = it,
                    color = Color.Red
                )
            }
        }
        OutlinedTextField(
            value = loginUiState.currentPassword,
            onValueChange = {
                loginViewModel.updatePassword(it)
            },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "Password") },
            placeholder = { Text(text = "Enter your password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Row {
            loginUiState.currentPasswordErrors.forEach {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .wrapContentWidth(Alignment.Start)
                        .fillMaxWidth(),
                    text = it,
                    color = Color.Red
                )
            }
        }
        Row{
            Text("Forget your password",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        bottom = 20.dp
                    )
                    .wrapContentWidth(Alignment.End)
                    .clickable {
                        navController.navigate(Routes.Forget.name)
                    },
                color = Color.Blue)
        }
        Button(
            onClick = {
                loginViewModel.login(navController,context)
                focusManager.clearFocus()
            },
            enabled = loginUiState.valid.value,
        ) {
            Text(
                "Login", modifier = Modifier.padding(
                    start = 30.dp,
                    end = 30.dp,
                )
            )
        }
        ClickableText(text = AnnotatedString("Don't have an account? Sign up"),
            onClick = { navController.navigate(Routes.Register.name)},
            modifier = Modifier.padding(
                top = 20.dp
            ),
            style = TextStyle(color = Color.Blue),
        )
        if(loginUiState.loading.value) {
            val strokeWidth = 5.dp
            CircularProgressIndicator(
                modifier = Modifier.drawBehind {
                    drawCircle(
                        Color.Blue,
                        radius = size.width / 2 - strokeWidth.toPx() / 2,
                        style = Stroke(strokeWidth.toPx())
                    )
                },
                color = Color.LightGray,
                strokeWidth = strokeWidth,
            )
        }
    }
}