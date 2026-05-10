package com.diskman.ui.purchases

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
import com.diskman.exceptions.ElementNotFoundException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.models.Purchase
import com.diskman.state.InventoryState
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.textField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Find Purchase Screen. It helps you find a purchase from the system.
 * */
@Composable
fun FindPurchaseScreen(state: InventoryState) {
    var id by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var purchaseInfo by remember { mutableStateOf<Purchase?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Buscar Compra")

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
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(30.dp)
                ) {
                    Text(text = "Buscar Compra:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    textField(
                        id,
                        onValueChange = { id = it.uppercase() },
                        "Introduce el ID de la compra:",
                        400
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            try {
                                purchaseInfo = null
                                if (id.isBlank()) throw InvalidFormatException("Rellena el campo.")

                                val purchase = state.findPurchase(id)
                                purchaseInfo = purchase
                                successMessage = "Búsqueda realizada con éxito."
                                scope.launch { delay(3000.milliseconds); successMessage = "" }
                                id = ""
                            } catch (e: ElementNotFoundException) {
                                errorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); errorMessage = "" }
                                id = ""
                            } catch (e: InvalidFormatException) {
                                errorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); errorMessage = "" }
                                id = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                        modifier = Modifier.width(400.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Buscar Compra")
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    if (errorMessage.isNotEmpty()) Text(text = errorMessage, color = Color.Red)
                    if (successMessage.isNotEmpty()) Text(text = successMessage, color = Color.Green)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            purchaseInfo?.let { purchase ->
                Card(
                    modifier = Modifier
                        .width(550.dp)
                        .padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "ID: ${purchase.idPurchase}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "${purchase.amount}€", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Text(text = "Fecha: ${purchase.purchaseDate}", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Discos (${purchase.vinylRecords.size}):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        purchase.vinylRecords.forEach { vinyl ->
                            Text(
                                text = "· ${vinyl.idVinyl} — ${vinyl.vinylName} | ${vinyl.band}",
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}