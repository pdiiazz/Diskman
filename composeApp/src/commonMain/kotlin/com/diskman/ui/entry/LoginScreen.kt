package com.diskman.ui.entry

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.exceptions.ElementNotFoundException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.models.User
import com.diskman.state.InventoryState
import diskman.composeapp.generated.resources.Res
import diskman.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Login screen. It allows you to access to the menu.
 * */
@Composable
fun LoginScreen(state: InventoryState) {
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val emailRegex = "^[a-z0-9+_.-]+@(.+)$".toRegex()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card ( modifier = Modifier .width(700.dp) .height(750.dp) .padding(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                // Logo
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo Diskman",
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.CenterHorizontally)
                )
                // Login Text
                Text(
                    text = "Inicia Sesión:", fontWeight = FontWeight.Bold, fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Email
                com.diskman.ui.others.emailTextField(email, onValueChange = { email = it }, "Email:")

                // Password
                com.diskman.ui.others.passwordTextField(password, onValueChange = { password = it }, "Contraseña:")

                Spacer(modifier = Modifier.height(8.dp))

                // Button to send
                Button(onClick = {
                    try {
                        if (email.isBlank() || password.isBlank()) throw InvalidFormatException("Rellena los campos correctamente")

                        val user = state.findUser(email)
                        if (!emailRegex.matches(email)) throw InvalidFormatException("Formato de correo electrónico incorrecto.")

                        if (password == user.password) {
                            when (user) {
                                is User.AdminUser -> state.login(user, com.diskman.ui.Screen.ADMIN_MENU)
                                is User.StandardUser -> state.login(user, com.diskman.ui.Screen.STANDARD_MENU)
                            }
                        } else {
                           errorMessage = "La contraseña no coincide con este usuario."
                        }
                    } catch (e: ElementNotFoundException) {
                        errorMessage = e.localizedMessage
                        scope.launch {
                            delay(3000.milliseconds)
                            errorMessage = ""
                        }
                    } catch (e: InvalidFormatException) {
                        errorMessage = e.localizedMessage
                        scope.launch {
                            delay(3000.milliseconds)
                            errorMessage = ""
                        }
                    } }, modifier = Modifier.width(550.dp) .height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)), shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = Color(0xFF1A237E),
                    modifier = Modifier.clickable {
                        state.changePage(com.diskman.ui.Screen.REGISTER)
                    }
                )
            }
        }
    }


}