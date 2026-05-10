package com.diskman.ui.vinyls

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.state.InventoryState
import androidx.compose.runtime.rememberCoroutineScope
import com.diskman.exceptions.DuplicateIdException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.ui.others.DiskmanHeader
import com.diskman.ui.others.textField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Add Vinyl Screen. It helps you to add a new vinyl record to the system.
 * */
@Composable
fun AddVinylScreen(state: InventoryState) {
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var band by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var edition by remember { mutableStateOf("") }
    var stamp by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var coverState by remember { mutableStateOf("") }
    var diskState by remember { mutableStateOf("") }
    var replace by remember { mutableStateOf(false) }
    var collectable by remember {mutableStateOf(false)}

    Column(modifier = Modifier.fillMaxSize()) {
       DiskmanHeader(state, "Añadir Disco de Vinilo")

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
                        text = "Añadir Disco de Vinilo:",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // ID + Name
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            id,
                            onValueChange = { id = it },
                            "Matriz del disco:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            name,
                            onValueChange = { name = it },
                            "Nombre del álbum:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Band + Release Year
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            band,
                            onValueChange = { band = it },
                            "Banda / Artista:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            year,
                            onValueChange = { year = it },
                            "Año de lanzamiento:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Country + Stamp
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            edition,
                            onValueChange = { edition = it },
                            "País de edición:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            stamp,
                            onValueChange = { stamp = it },
                            "Sello discográfico:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Genre + Style
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            genre,
                            onValueChange = { genre = it },
                            "Género musical:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            style,
                            onValueChange = { style = it },
                            "Estilo:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Category + Cover State
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            category,
                            onValueChange = { category = it.uppercase() },
                            "Categoría:",
                            300
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        textField(
                            coverState,
                            onValueChange = { coverState = it.uppercase() },
                            "Estado carátula:",
                            300
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Record State
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textField(
                            diskState,
                            onValueChange = { diskState = it.uppercase() },
                            "Estado del disco:",
                            610
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        // Checks
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = replace,
                                onCheckedChange = { replace = it},
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF1A1F5E),
                                    checkmarkColor = Color.White
                                )
                            )
                            Text(
                                text = "¿Para cambiar en un futuro?"
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = collectable,
                                onCheckedChange = { collectable = it},
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF1A1F5E),
                                    checkmarkColor = Color.White
                                )
                            )
                            Text(
                                text = "¿Para cambiar tu colección?"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Send Button
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                try {
                                    if (id.isBlank() || name.isBlank() || band.isBlank() || year.isBlank() ||
                                        edition.isBlank() || stamp.isBlank() || genre.isBlank() || style.isBlank() ||
                                        category.isBlank() || coverState.isBlank() || diskState.isBlank()
                                    ) throw InvalidFormatException("Rellena todos los campos.")

                                    if (state.vinylExists(id)) throw DuplicateIdException("Ya hay un disco con esta matriz.")

                                    if (year.length != 4 || !year.all {it.isDigit()}) throw InvalidFormatException("El año debe tener 4 dígitos.")

                                    val categories = listOf("LP", "S", "EP", "MS", "MLP")
                                    val states = listOf("M","NM", "VG+", "VG", "G+", "G", "F", "P")

                                    if (!categories.contains(category)) throw InvalidFormatException("Formato de categoria incorrecto. (LP, S (Single), EP, MS (Maxi Single), MLP (Mini LP))")
                                    if (!states.contains(coverState)) throw InvalidFormatException("Formato de estado de la caratula incorrecto. (M, NM, VG+, VG, G+, G, F, P)")
                                    if (!states.contains(diskState)) throw InvalidFormatException("Formato de estado del disco incorrecto. (M, NM, VG+, VG, G+, G, F, P)")

                                    val vinyl = state.createVinyl(id, name, band, year, edition, stamp, genre, style, category, coverState, diskState, replace, collectable)
                                    state.addVinylRecord(vinyl)
                                    state.save()

                                    successMessage = "¡Disco $name añadido con éxito!"

                                    scope.launch {
                                        delay(3000.milliseconds)
                                        successMessage = ""
                                    }

                                    id = ""
                                    name = ""
                                    band = ""
                                    year = ""
                                    category = ""
                                    edition = ""
                                    stamp = ""
                                    genre = ""
                                    style = ""
                                    category = ""
                                    coverState = ""
                                    diskState = ""
                                } catch (e: InvalidFormatException) {
                                    errorMessage = e.localizedMessage
                                    scope.launch {
                                        delay(3000.milliseconds)
                                        errorMessage = ""
                                    }
                                } catch (e: DuplicateIdException){
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
                            Text("Guardar Disco")
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
