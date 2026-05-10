package com.diskman.ui.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.emailTextField
import com.diskman.ui.others.passwordTextField
import com.diskman.ui.others.tryCreateUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Add User Screen. It helps you to add a new user to the system
 */
@Composable
fun AddUserScreen(state: InventoryState) {
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var privileges by remember {mutableStateOf(false)}

    val emailRegex = "^[a-z0-9+_.-]+@(.+)$".toRegex()
    val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$".toRegex()

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Añadir Usuario")

        // Main Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .width(700.dp)
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                ) {
                    Text(
                        text = "Añadir Usuario:",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // ID + Password
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                        emailTextField(
                            email,
                            onValueChange = { email = it },
                            "Email:"
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        passwordTextField(
                            password,
                            onValueChange = { password = it },
                            "Contraseña:",
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        passwordTextField(
                            password2,
                            onValueChange = { password2 = it },
                            "Repita la contraseña:"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Checks
                    Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        // Checks
                        Checkbox(
                            checked = privileges,
                            onCheckedChange = { privileges = it},
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF1A1F5E),
                                checkmarkColor = Color.White
                            )
                        )
                        Text(
                            text = "¿Quieres que sea usuario administrador?"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Send Button
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                try {
                                    tryCreateUser(
                                        state,
                                        email,
                                        password,
                                        password2,
                                        emailRegex,
                                        passwordRegex
                                    )

                                    val user = state.createUser(email.trim().lowercase(), password.trim(), privileges)
                                    state.addUser(user)
                                    state.save()

                                    successMessage = "Usuario añadido con éxito"

                                    scope.launch {
                                        delay(3000.milliseconds)
                                        successMessage= ""
                                    }

                                    email = ""
                                    password = ""
                                    password2 = ""
                                    privileges = false
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
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                            modifier = Modifier.width(550.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Guardar Usuario")
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red
                        )
                    }
                    if (successMessage.isNotEmpty()) {
                        Text(
                            text = successMessage,
                            color = Color.Green
                        )
                    }
                }
            }
        }
    }
}