package com.diskman.ui.entry

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.exceptions.DuplicateIdException
import com.diskman.exceptions.ElementNotFoundException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.exceptions.PasswordMismatchException
import com.diskman.state.InventoryState
import diskman.composeapp.generated.resources.Res
import diskman.composeapp.generated.resources.logo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.milliseconds

/**
 * Register Screen. It allows you to register a new account.
 * */
@Composable
fun RegisterScreen(state: InventoryState) {
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var repeatedPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val emailRegex = "^[a-z0-9+_.-]+@(.+)$".toRegex()
    val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$".toRegex()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card ( modifier = Modifier .width(700.dp) .height(800.dp) .padding(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
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
                    text = "Registrarse:", fontWeight = FontWeight.Bold, fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Email
                com.diskman.ui.others.emailTextField(email, { email = it }, "Email:")

                // Password
                com.diskman.ui.others.passwordTextField(password, { password = it }, "Contraseña:")
                com.diskman.ui.others.passwordTextField(
                    repeatedPassword,
                    { repeatedPassword = it },
                    "Repita la contraseña:"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Button to send
                Button(onClick = {
                    try {
                        com.diskman.ui.others.tryCreateUser(
                            state,
                            email,
                            password,
                            repeatedPassword,
                            emailRegex,
                            passwordRegex
                        )

                        val user = state.createUser(email, password, false)
                        state.addUser(user)
                        state.save()

                        state.login(user, com.diskman.ui.Screen.STANDARD_MENU)
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
                    } catch (e: PasswordMismatchException) {
                        errorMessage = e.localizedMessage
                        scope.launch {
                            delay(3000.milliseconds)
                            errorMessage = ""
                        }
                    } catch (e: DuplicateIdException) {
                        errorMessage = e.localizedMessage
                        scope.launch {
                            delay(3000.milliseconds)
                            errorMessage = ""
                        }
                    }
                }, modifier = Modifier.width(550.dp) .height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)), shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Registrarse")
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
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    color = Color(0xFF1A237E),
                    modifier = Modifier.clickable {
                        state.changePage(com.diskman.ui.Screen.LOGIN)
                    }
                )
            }
        }
    }


}