package com.diskman.ui.others

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.exceptions.DuplicateIdException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.exceptions.PasswordMismatchException
import com.diskman.models.User
import com.diskman.state.InventoryState
import diskman.composeapp.generated.resources.Res
import diskman.composeapp.generated.resources.small
import org.jetbrains.compose.resources.painterResource

/** DON'T REPEAT YOURSELF, classes and functions. It helps you to don't repeat the same pieces of code.*/
// For email Inputs
@Composable
fun emailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.width(550.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1A237E),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color(0xFF1A237E),
            unfocusedLabelColor = Color.Gray
        ),
        singleLine = true
    )
}

// For password Inputs
@Composable
fun passwordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.width(550.dp),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1A237E),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color(0xFF1A237E),
            unfocusedLabelColor = Color.Gray
        )
    )
}

// For textFields
@Composable
fun textField (
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    width: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.width(width.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1A237E),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color(0xFF1A237E),
            unfocusedLabelColor = Color.Gray
        ),
        singleLine = true
    )
}

// For Cards
@Composable
fun MenuCard(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(text = description)

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.CenterHorizontally) .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(buttonText)
            }
        }
    }
}

@Composable
fun DiskmanHeader(
    state: InventoryState,
    subtitle: String
) {
    val user = state.currentUser.value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF141A66))
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Image(
                painter = painterResource(Res.drawable.small),
                contentDescription = "Logo Diskman",
                modifier = Modifier.size(100.dp)
            )
            Column {
                Text(text = "Diskman", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 40.sp)
                Text(text = subtitle, color = Color(0xFFB0BEC5), fontSize = 24.sp)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(horizontalAlignment = Alignment.End) {
                Text(text = user?.idUser ?: "Anónimo", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 20.sp)
                Text(
                    text = when (user) {
                        is User.AdminUser -> "Administrador"
                        is User.StandardUser -> "Usuario Estándar"
                        else -> "?"
                    },
                    color = Color(0xFFB0BEC5),
                    fontSize = 17.sp
                )
            }
            Spacer(modifier = Modifier.width(30.dp))

            when (state.currentUser.value) {
                is User.AdminUser -> {
                    Button(
                        onClick = { state.changePage(com.diskman.ui.Screen.ADMIN_MENU) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6FA5)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 26.dp, vertical = 8.dp)
                    ) {
                        Text("Volver al Menú", fontSize = 13.sp, color = Color.White)
                    }
                }

                is User.StandardUser -> {
                    Button(
                        onClick = { state.changePage(com.diskman.ui.Screen.STANDARD_MENU) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6FA5)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 26.dp, vertical = 8.dp)
                    ) {
                        Text("Volver al Menú", fontSize = 13.sp, color = Color.White)
                    }
                }

                else -> {
                    Button(
                        onClick = { state.changePage(com.diskman.ui.Screen.LOGIN) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6FA5)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 26.dp, vertical = 8.dp)
                    ) {
                        Text("Volver al Menú", fontSize = 13.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.width(5.dp))
            Button(
                onClick = { state.logout() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF546E7A)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 26.dp, vertical = 8.dp)
            ) {
                Text("Cerrar sesión", fontSize = 13.sp, color = Color.White)
            }
        }
    }
}

fun tryCreateUser(state: InventoryState, email: String, password: String, password2: String, emailRegex: Regex, passwordRegex: Regex) {
    if (state.userExists(email)) throw DuplicateIdException("Este correo ya tiene una cuenta registrada.")

    if (email.isBlank() || password.isBlank() || password2.isBlank()) throw InvalidFormatException("Rellena los campos correctamente")
    if (!emailRegex.matches(email)) throw InvalidFormatException("Formato de correo electrónico incorrecto. (example@diskman.com)")
    if (!passwordRegex.matches(password)) throw InvalidFormatException("Formato de contraseña incorrecto. (Mínimo 1 mayúscula, 8 carácteres y un número)")
    if (password != password2) throw PasswordMismatchException("Las contraseñas no coinciden.")
}

class VinylFormState {
    var id by mutableStateOf("")
    var name by mutableStateOf("")
    var band by mutableStateOf("")
    var year by mutableStateOf("")
    var edition by mutableStateOf("")
    var stamp by mutableStateOf("")
    var genre by mutableStateOf("")
    var style by mutableStateOf("")
    var category by mutableStateOf("")
    var coverState by mutableStateOf("")
    var diskState by mutableStateOf("")
    var replace by mutableStateOf(false)
    var collectable by mutableStateOf(false)

    fun clear() {
        id = ""; name = ""; band = ""; year = ""; edition = ""
        stamp = ""; genre = ""; style = ""; category = ""
        coverState = ""; diskState = ""; replace = false; collectable = false
    }
}