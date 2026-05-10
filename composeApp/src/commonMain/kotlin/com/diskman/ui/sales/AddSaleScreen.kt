package com.diskman.ui.sales

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
import com.diskman.exceptions.DuplicateIdException
import com.diskman.exceptions.ElementNotFoundException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.exceptions.InvalidSaleException
import com.diskman.models.VinylRecord
import com.diskman.state.InventoryState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.runtime.mutableStateListOf
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.textField

/**
 * Add Sale Screen. It helps you to add a new sale to the system.
 * */
@Composable
fun AddSaleScreen(state: InventoryState) {
    val scope = rememberCoroutineScope()
    var saleErrorMessage by remember { mutableStateOf("") }
    var saleSuccessMessage by remember { mutableStateOf("") }
    var vinylErrorMessage by remember { mutableStateOf("") }
    var vinylSuccessMessage by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var vinylId by remember { mutableStateOf("") }
    val vinylList = remember { mutableStateListOf<VinylRecord>() }

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Añadir Venta")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.width(700.dp).padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(30.dp)
                ) {
                    Text("Datos de la Venta:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            amount,
                            onValueChange = { amount = it },
                            "Importe (€):",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            date,
                            onValueChange = { date = it },
                            "Fecha (YYYY-MM-DD):",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            try {
                                if (amount.isBlank() || date.isBlank())
                                    throw InvalidFormatException("Rellena el importe y la fecha.")
                                if (vinylList.isEmpty())
                                    throw InvalidFormatException("Añade al menos un disco a la venta.")
                                val parsedAmount = amount.trim().toDoubleOrNull()
                                    ?: throw InvalidFormatException("El importe debe ser un número válido. (0.00)")
                                val parsedDate = LocalDate.parse(date.trim())

                                val sale = state.createSale(parsedAmount, parsedDate)
                                vinylList.forEach { sale.addVinylRecord(it) }
                                state.sellVinylRecords(sale)
                                state.save()
                                saleSuccessMessage = "¡Venta guardada con ${vinylList.size} disco(s)!"
                                vinylList.clear()
                                amount = ""
                                date = ""
                                scope.launch { delay(3000.milliseconds); saleSuccessMessage = "" }
                            } catch (e: InvalidFormatException) {
                                saleErrorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); saleErrorMessage = "" }
                            } catch (e: IllegalArgumentException) {
                                saleErrorMessage = "Formato de fecha incorrecto. Usa YYYY-MM-DD."
                                scope.launch { delay(3000.milliseconds); saleErrorMessage = "" }
                            } catch (e: InvalidSaleException) {
                                saleErrorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); saleErrorMessage = "" }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                        modifier = Modifier.width(610.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Guardar Venta")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (saleErrorMessage.isNotEmpty()) Text(text = saleErrorMessage, color = Color.Red)
                    if (saleSuccessMessage.isNotEmpty()) Text(text = saleSuccessMessage, color = Color.Green)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.width(700.dp).padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(30.dp)
                ) {
                    Text("Añadir Disco a la Venta:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    textField(
                        vinylId,
                        onValueChange = { vinylId = it },
                        "Matriz del disco:",
                        610
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            try {
                                if (vinylId.isBlank()) throw InvalidFormatException("Introduce la matriz del disco.")
                                if (vinylList.any { it.idVinyl == vinylId.trim() })
                                    throw DuplicateIdException("Este disco ya está en la venta.")
                                val vinyl = state.findVinylRecord(vinylId)
                                vinylList.add(vinyl)
                                vinylSuccessMessage = "Disco ${vinyl.vinylName} añadido a la venta."
                                vinylId = ""
                                scope.launch { delay(2000.milliseconds); vinylSuccessMessage = "" }
                            } catch (e: InvalidFormatException) {
                                vinylErrorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); vinylErrorMessage = "" }
                            } catch (e: ElementNotFoundException) {
                                vinylErrorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); vinylErrorMessage = "" }
                            } catch (e: DuplicateIdException) {
                                vinylErrorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); vinylErrorMessage = "" }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6FA5)),
                        modifier = Modifier.width(610.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Añadir Disco a la Venta")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (vinylErrorMessage.isNotEmpty()) Text(text = vinylErrorMessage, color = Color.Red)
                    if (vinylSuccessMessage.isNotEmpty()) Text(text = vinylSuccessMessage, color = Color.Green)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (vinylList.isNotEmpty()) {
                Card(
                    modifier = Modifier.width(700.dp).padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(30.dp)
                    ) {
                        Text("Discos en esta venta:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        vinylList.forEach { vinyl ->
                            Text(text = "· ${vinyl.idVinyl} — ${vinyl.vinylName} | ${vinyl.band}", fontSize = 13.sp, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}