package com.diskman.ui.entry

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun RegisterScreen(state: InventoryState) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val emailRegex = "^[a-z0-9+_.-]+@(.+)$".toRegex()
    val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$".toRegex()

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val isMobile = maxWidth < 600.dp
        val cardWidth = if (isMobile) maxWidth - 32.dp else 700.dp
        val logoSize = if (isMobile) 120.dp else 300.dp

        Card(
            modifier = Modifier.width(cardWidth).padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(vertical = 24.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo Diskman",
                    modifier = Modifier.size(logoSize)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Registrarse:", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(16.dp))

                com.diskman.ui.others.emailTextField(email, { email = it }, "Email:")
                Spacer(modifier = Modifier.height(8.dp))
                com.diskman.ui.others.passwordTextField(password, { password = it }, "Contraseña:")
                Spacer(modifier = Modifier.height(8.dp))
                com.diskman.ui.others.passwordTextField(repeatedPassword, { repeatedPassword = it }, "Repita la contraseña:")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        try {
                            com.diskman.ui.others.tryCreateUser(state, email, password, repeatedPassword, emailRegex, passwordRegex)
                            val user = state.createUser(email, password, false)
                            state.addUser(user)
                            state.save()
                            state.login(user, com.diskman.ui.Screen.STANDARD_MENU)
                        } catch (e: ElementNotFoundException) {
                            errorMessage = e.localizedMessage
                            scope.launch { delay(3000.milliseconds); errorMessage = "" }
                        } catch (e: InvalidFormatException) {
                            errorMessage = e.localizedMessage
                            scope.launch { delay(3000.milliseconds); errorMessage = "" }
                        } catch (e: PasswordMismatchException) {
                            errorMessage = e.localizedMessage
                            scope.launch { delay(3000.milliseconds); errorMessage = "" }
                        } catch (e: DuplicateIdException) {
                            errorMessage = e.localizedMessage
                            scope.launch { delay(3000.milliseconds); errorMessage = "" }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Registrarse")
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    color = Color(0xFF1A237E),
                    modifier = Modifier.clickable { state.changePage(com.diskman.ui.Screen.LOGIN) }
                )
            }
        }
    }
}