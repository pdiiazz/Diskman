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

@Composable
fun LoginScreen(state: InventoryState) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val emailRegex = "^[a-z0-9+_.-]+@(.+)$".toRegex()

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val isMobile = maxWidth < 600.dp
        val cardWidth = if (isMobile) maxWidth - 32.dp else 700.dp
        val logoSize = if (isMobile) 140.dp else 300.dp
        val titleSize = if (isMobile) 16.sp else 18.sp

        Card(
            modifier = Modifier
                .width(cardWidth)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo Diskman",
                    modifier = Modifier.size(logoSize).align(Alignment.CenterHorizontally)
                )

                Text(text = "Inicia Sesión:", fontWeight = FontWeight.Bold, fontSize = titleSize)

                Spacer(modifier = Modifier.height(24.dp))

                com.diskman.ui.others.emailTextField(email, onValueChange = { email = it }, "Email:")
                Spacer(modifier = Modifier.height(8.dp))
                com.diskman.ui.others.passwordTextField(password, onValueChange = { password = it }, "Contraseña:")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
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
                            scope.launch { delay(3000.milliseconds); errorMessage = "" }
                        } catch (e: InvalidFormatException) {
                            errorMessage = e.localizedMessage
                            scope.launch { delay(3000.milliseconds); errorMessage = "" }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = Color(0xFF1A237E),
                    modifier = Modifier.clickable { state.changePage(com.diskman.ui.Screen.REGISTER) }
                )
            }
        }
    }
}