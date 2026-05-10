package com.diskman.ui.vinyls

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.exceptions.DuplicateIdException
import com.diskman.exceptions.InvalidFormatException
import com.diskman.state.InventoryState
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
    var collectable by remember { mutableStateOf(false) }

    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(LocalDensity.current) { windowInfo.containerSize.width.toDp() }
    val screenHeight = with(LocalDensity.current) { windowInfo.containerSize.height.toDp() }
    val isMobile = screenWidth < 800.dp
    val cardWidth = if (isMobile) screenWidth - 32.dp else 700.dp

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state, "Añadir Disco de Vinilo")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = screenHeight),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.width(cardWidth).padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(20.dp)
                    ) {
                        Text("Añadir Disco de Vinilo:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (isMobile) {
                            textField(id, { id = it }, "Matriz del disco:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(name, { name = it }, "Nombre del álbum:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(band, { band = it }, "Banda / Artista:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(year, { year = it }, "Año de lanzamiento:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(edition, { edition = it }, "País de edición:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(stamp, { stamp = it }, "Sello discográfico:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(genre, { genre = it }, "Género musical:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(style, { style = it }, "Estilo:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(category, { category = it.uppercase() }, "Categoría:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(coverState, { coverState = it.uppercase() }, "Estado carátula:", 300)
                            Spacer(modifier = Modifier.height(8.dp))
                            textField(diskState, { diskState = it.uppercase() }, "Estado del disco:", 300)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                textField(id, { id = it }, "Matriz del disco:", 300)
                                Spacer(modifier = Modifier.width(10.dp))
                                textField(name, { name = it }, "Nombre del álbum:", 300)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                textField(band, { band = it }, "Banda / Artista:", 300)
                                Spacer(modifier = Modifier.width(10.dp))
                                textField(year, { year = it }, "Año de lanzamiento:", 300)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                textField(edition, { edition = it }, "País de edición:", 300)
                                Spacer(modifier = Modifier.width(10.dp))
                                textField(stamp, { stamp = it }, "Sello discográfico:", 300)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                textField(genre, { genre = it }, "Género musical:", 300)
                                Spacer(modifier = Modifier.width(10.dp))
                                textField(style, { style = it }, "Estilo:", 300)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                textField(category, { category = it.uppercase() }, "Categoría:", 300)
                                Spacer(modifier = Modifier.width(10.dp))
                                textField(coverState, { coverState = it.uppercase() }, "Estado carátula:", 300)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            textField(diskState, { diskState = it.uppercase() }, "Estado del disco:", 610)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isMobile) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = replace, onCheckedChange = { replace = it }, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1A1F5E), checkmarkColor = Color.White))
                                    Text("¿Para cambiar en un futuro?", fontSize = 13.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = collectable, onCheckedChange = { collectable = it }, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1A1F5E), checkmarkColor = Color.White))
                                    Text("¿Para la colección?", fontSize = 13.sp)
                                }
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = replace, onCheckedChange = { replace = it }, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1A1F5E), checkmarkColor = Color.White))
                                    Text("¿Para cambiar en un futuro?")
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = collectable, onCheckedChange = { collectable = it }, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1A1F5E), checkmarkColor = Color.White))
                                    Text("¿Para cambiar tu colección?")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                try {
                                    if (id.isBlank() || name.isBlank() || band.isBlank() || year.isBlank() ||
                                        edition.isBlank() || stamp.isBlank() || genre.isBlank() || style.isBlank() ||
                                        category.isBlank() || coverState.isBlank() || diskState.isBlank()
                                    ) throw InvalidFormatException("Rellena todos los campos.")
                                    if (state.vinylExists(id)) throw DuplicateIdException("Ya hay un disco con esta matriz.")
                                    if (year.length != 4 || !year.all { it.isDigit() }) throw InvalidFormatException("El año debe tener 4 dígitos.")
                                    val categories = listOf("LP", "S", "EP", "MS", "MLP")
                                    val states = listOf("M", "NM", "VG+", "VG", "G+", "G", "F", "P")
                                    if (!categories.contains(category)) throw InvalidFormatException("Categoría incorrecta. (LP, S, EP, MS, MLP)")
                                    if (!states.contains(coverState)) throw InvalidFormatException("Estado carátula incorrecto. (M, NM, VG+, VG, G+, G, F, P)")
                                    if (!states.contains(diskState)) throw InvalidFormatException("Estado disco incorrecto. (M, NM, VG+, VG, G+, G, F, P)")
                                    val vinyl = state.createVinyl(id, name, band, year, edition, stamp, genre, style, category, coverState, diskState, replace, collectable)
                                    state.addVinylRecord(vinyl)
                                    state.save()
                                    successMessage = "¡Disco $name añadido con éxito!"
                                    scope.launch { delay(3000.milliseconds); successMessage = "" }
                                    id = ""; name = ""; band = ""; year = ""; edition = ""; stamp = ""
                                    genre = ""; style = ""; category = ""; coverState = ""; diskState = ""
                                } catch (e: InvalidFormatException) {
                                    errorMessage = e.localizedMessage
                                    scope.launch { delay(3000.milliseconds); errorMessage = "" }
                                } catch (e: DuplicateIdException) {
                                    errorMessage = e.localizedMessage
                                    scope.launch { delay(3000.milliseconds); errorMessage = "" }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1F5E)),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) { Text("Guardar Disco") }

                        Spacer(modifier = Modifier.height(5.dp))
                        if (errorMessage.isNotEmpty()) Text(errorMessage, color = Color.Red)
                        if (successMessage.isNotEmpty()) Text(successMessage, color = Color.Green)
                    }
                }
            }
        }
    }
}