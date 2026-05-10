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
import com.diskman.exceptions.DuplicateIdException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.models.VinylRecord
import com.diskman.state.InventoryState
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.VinylFormState
import com.diskman.ui.others.textField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.milliseconds

/**
 * Add Purchase Screen. It helps you to add a new purchase to the system.
 * */
@Composable
fun AddPurchaseScreen(state: InventoryState) {
    val scope = rememberCoroutineScope()

    var purchaseSuccessMessage by remember { mutableStateOf("") }
    var purchaseErrorMessage by remember { mutableStateOf("") }
    var vinylSuccessMessage by remember { mutableStateOf("") }
    var vinylErrorMessage by remember { mutableStateOf("") }

    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val vinylForm = remember { VinylFormState() }
    val vinylList = remember { mutableStateListOf<VinylRecord>() }

    val categories = listOf("LP", "S", "EP", "MS", "MLP")
    val states = listOf("M", "NM", "VG+", "VG", "G+", "G", "F", "P")

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Añadir Compra")

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
                    Text("Datos de la Compra:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
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
                                    throw InvalidFormatException("Añade al menos un disco a la compra.")
                                val parsedAmount = amount.trim().toDoubleOrNull()
                                    ?: throw InvalidFormatException("El importe debe ser un número válido. (0.00)")
                                val parsedDate = LocalDate.parse(date.trim())

                                val purchase = state.createPurchase(parsedAmount, parsedDate)
                                vinylList.forEach { purchase.addVinylRecord(it) }
                                state.buyVinylRecords(purchase)
                                state.save()
                                purchaseSuccessMessage = "¡Compra guardada con ${vinylList.size} disco(s)!"
                                vinylList.clear()
                                amount = ""
                                date = ""
                                scope.launch {
                                    delay(3000.milliseconds); purchaseSuccessMessage = ""
                                }
                            } catch (e: InvalidFormatException) {
                                purchaseErrorMessage = e.localizedMessage
                                scope.launch { delay(3000.milliseconds); purchaseErrorMessage = "" }
                            } catch (e: IllegalArgumentException) {
                                purchaseErrorMessage = "Formato de fecha incorrecto. Usa YYYY-MM-DD."
                                scope.launch { delay(3000.milliseconds); purchaseErrorMessage = "" }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                        modifier = Modifier.width(610.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Guardar Compra")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if (purchaseErrorMessage.isNotEmpty()) Text(text = purchaseErrorMessage, color = Color.Red)
                    if (purchaseSuccessMessage.isNotEmpty()) Text(text = purchaseSuccessMessage, color = Color.Green)
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
                    Text("Añadir Disco:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            vinylForm.id,
                            onValueChange = { vinylForm.id = it },
                            "Matriz:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            vinylForm.name,
                            onValueChange = { vinylForm.name = it },
                            "Nombre del álbum:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            vinylForm.band,
                            onValueChange = { vinylForm.band = it },
                            "Banda / Artista:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            vinylForm.year,
                            onValueChange = { vinylForm.year = it },
                            "Año:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            vinylForm.edition,
                            onValueChange = { vinylForm.edition = it },
                            "País de edición:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            vinylForm.stamp,
                            onValueChange = { vinylForm.stamp = it },
                            "Sello discográfico:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            vinylForm.genre,
                            onValueChange = { vinylForm.genre = it },
                            "Género musical:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            vinylForm.style,
                            onValueChange = { vinylForm.style = it },
                            "Estilo:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            vinylForm.category,
                            onValueChange = { vinylForm.category = it.uppercase() },
                            "Categoría:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            vinylForm.coverState,
                            onValueChange = { vinylForm.coverState = it.uppercase() },
                            "Estado carátula:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            vinylForm.diskState,
                            onValueChange = { vinylForm.diskState = it.uppercase() },
                            "Estado del disco:",
                            610
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = vinylForm.replace,
                                onCheckedChange = { vinylForm.replace = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1A1F5E), checkmarkColor = Color.White)
                            )
                            Text("¿Para cambiar en un futuro?")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = vinylForm.collectable,
                                onCheckedChange = { vinylForm.collectable = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1A1F5E), checkmarkColor = Color.White)
                            )
                            Text("¿Para la colección?")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            try {
                                if (vinylForm.id.isBlank() || vinylForm.name.isBlank() || vinylForm.band.isBlank() ||
                                    vinylForm.year.isBlank() || vinylForm.edition.isBlank() || vinylForm.stamp.isBlank() ||
                                    vinylForm.genre.isBlank() || vinylForm.style.isBlank() || vinylForm.category.isBlank() ||
                                    vinylForm.coverState.isBlank() || vinylForm.diskState.isBlank()
                                ) throw InvalidFormatException("Rellena todos los campos del disco.")

                                if (vinylForm.year.length != 4 || !vinylForm.year.all { it.isDigit() })
                                    throw InvalidFormatException("El año debe tener 4 dígitos.")

                                if (!categories.contains(vinylForm.category))
                                    throw InvalidFormatException("Categoría incorrecta. (LP, S, EP, MS, MLP)")

                                if (!states.contains(vinylForm.coverState))
                                    throw InvalidFormatException("Estado carátula incorrecto. (M, NM, VG+, VG, G+, G, F, P)")

                                if (!states.contains(vinylForm.diskState))
                                    throw InvalidFormatException("Estado disco incorrecto. (M, NM, VG+, VG, G+, G, F, P)")

                                if (vinylList.any { it.idVinyl == vinylForm.id.trim() } || state.vinylExists(vinylForm.id))
                                    throw DuplicateIdException("Ya existe un disco con esta matriz.")

                                val vinyl = VinylRecord(
                                    vinylForm.id.trim(), vinylForm.name.trim(), vinylForm.band.trim(),
                                    vinylForm.year.trim().toInt(), vinylForm.edition.trim(), vinylForm.stamp.trim(),
                                    vinylForm.genre.trim(), vinylForm.style.trim(), vinylForm.category.trim(),
                                    vinylForm.coverState.trim(), vinylForm.diskState.trim(),
                                    vinylForm.replace, vinylForm.collectable
                                )
                                vinylList.add(vinyl)
                                vinylSuccessMessage = "Disco ${vinylForm.name} añadido a la compra."
                                vinylForm.clear()
                                scope.launch { delay(2000.milliseconds); vinylSuccessMessage = "" }

                            } catch (e: InvalidFormatException) {
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
                        Text("Añadir Disco a la Compra")
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
                        Text("Discos en esta compra:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        vinylList.forEach { vinyl ->
                            Text(text = vinyl.vinylInfo(), fontSize = 13.sp, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}