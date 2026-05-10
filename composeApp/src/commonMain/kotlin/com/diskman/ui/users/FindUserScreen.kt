package com.diskman.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.diskman.exceptions.ElementNotFoundException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.state.InventoryState
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.textField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Fin User Screen. It helps you find a user of the system.
 * */
@Composable
fun FindUserScreen(state: InventoryState) {
    var id by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var userInfo by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Buscar Usuario")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .width(550.dp)
                    .height(400.dp)
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Buscar Usuario:",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    textField(
                        id,
                        onValueChange = { id = it },
                        "Introduce el correo del usuario:",
                        400
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            try {
                                title = ""
                                userInfo = ""
                                if (id.isBlank()) throw InvalidFormatException("Rellena el campo.")

                                val user = state.findUser(id)
                                successMessage = "Búsqueda realizada con éxito."

                                title = "Información del Usuario:"
                                userInfo = user.info()

                                scope.launch {
                                    delay(3000.milliseconds)
                                    successMessage = ""
                                }
                                id = ""
                            } catch (e: ElementNotFoundException) {
                                errorMessage = e.localizedMessage
                                scope.launch {
                                    delay(3000.milliseconds)
                                    errorMessage = ""
                                }
                                id = ""
                            } catch (e: InvalidFormatException) {
                                errorMessage = e.localizedMessage
                                scope.launch {
                                    delay(3000.milliseconds)
                                    errorMessage = ""
                                }
                                id = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                        modifier = Modifier.width(400.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Buscar Usuario")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = Color.Red)
                    }
                    if (successMessage.isNotEmpty()) {
                        Text(text = successMessage, color = Color.Green)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (userInfo.isNotEmpty()) {
                Column (
                    modifier = Modifier.background(color = Color(0xFFEEEEEE)).padding(10.dp)
                ){
                    Text(
                        text = userInfo
                    )
                }
            }
        }
    }
}