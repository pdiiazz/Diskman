package com.diskman.ui.purchases

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
import com.diskman.exceptions.ElementNotFoundException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.state.InventoryState
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.textField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Delete Purchase Screen. It helps you to delete purchases from the system.
 * */
@Composable
fun DeletePurchaseScreen(state: InventoryState) {
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var id by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Borrar Compra")

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
                        text = "Borrar Compra:",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    textField(
                        id,
                        onValueChange = { id = it.uppercase() },
                        "ID de la compra:",
                        610
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            try {
                                if (id.isBlank()) throw InvalidFormatException("Introduce el ID de la compra.")

                                val purchase = state.findPurchase(id)
                                state.deletePurchase(purchase)
                                state.save()

                                successMessage = "¡Compra ${purchase.idPurchase} eliminada con éxito!"
                                id = ""
                                scope.launch {
                                    delay(3000.milliseconds)
                                    successMessage = ""
                                }
                            } catch (e: InvalidFormatException) {
                                errorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); errorMessage = "" }
                            } catch (e: ElementNotFoundException) {
                                errorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); errorMessage = "" }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                        modifier = Modifier.width(610.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Borrar Compra")
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    if (errorMessage.isNotEmpty()) Text(text = errorMessage, color = Color.Red)
                    if (successMessage.isNotEmpty()) Text(text = successMessage, color = Color.Green)
                }
            }
        }
    }
}