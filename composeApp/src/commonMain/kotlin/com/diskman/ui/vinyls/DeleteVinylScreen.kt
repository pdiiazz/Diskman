package com.diskman.ui.vinyls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.state.InventoryState
import androidx.compose.runtime.rememberCoroutineScope
import com.diskman.exceptions.ElementNotFoundException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.textField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Delete Vinyl Screen. It helps you to delete a vinyl record from the system.
 * */
@Composable
fun DeleteVinylScreen(state: InventoryState) {
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    var id by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Borrar Disco de Vinilo")

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
                        text = "Borrar Disco de Vinilo:",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    textField(
                        id,
                        onValueChange = { id = it },
                        "Matriz del disco:",
                        610
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            try {
                                if (id.isBlank()) throw InvalidFormatException("Introduce la matriz del disco.")

                                val vinyl = state.findVinylRecord(id)

                                state.deleteVinylRecord(vinyl)

                                state.save()

                                successMessage = "¡Disco ${vinyl.vinylName} eliminado con éxito!"
                                scope.launch {
                                    delay(3000.milliseconds)
                                    successMessage = ""
                                }
                                id = ""
                            } catch (e: InvalidFormatException) {
                                errorMessage = e.localizedMessage
                                scope.launch {
                                    delay(3000.milliseconds)
                                    errorMessage = ""
                                }
                            } catch (e: ElementNotFoundException){
                                errorMessage = e.localizedMessage
                                scope.launch {
                                    delay(3000.milliseconds)
                                    errorMessage = ""
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                        modifier = Modifier.width(610.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Borrar Disco")
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
        }
    }
}