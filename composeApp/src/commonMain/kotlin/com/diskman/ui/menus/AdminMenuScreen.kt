package com.diskman.ui.menus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.models.User
import com.diskman.state.InventoryState
import diskman.composeapp.generated.resources.Res
import diskman.composeapp.generated.resources.small
import org.jetbrains.compose.resources.painterResource

/**
 * Admin Menu Screen. It allows you to do superuser actions.
 * */
@Composable
fun AdminMenuScreen(state: InventoryState) {
    val user = state.currentUser.value

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF141A66))
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LOGO
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(Res.drawable.small),
                    contentDescription = "Logo Diskman",
                    modifier = Modifier.size(100.dp)
                )
                Column {
                    Text(
                        text = "Diskman",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                    Text(
                        text = "Panel de administración",
                        color = Color(0xFFB0BEC5),
                        fontSize = 24.sp
                    )
                }
            }

            // User Info + Logout Button
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = user?.idUser ?: "—",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                    Text(
                        text = when (user) {
                            is User.AdminUser -> "Administrador"
                            else -> "—"
                        },
                        color = Color(0xFFB0BEC5),
                        fontSize = 17.sp
                    )
                }
                Spacer( modifier = Modifier.width(30.dp))
                // Logout Button
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
        // Main Content
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
        ) {
            // What is Diskman section
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier .padding(horizontal = 100.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier .padding(vertical = 50.dp)
                ) {
                    Text(
                        text = "¿Qué es Diskman?",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text="Diskman es un programa de gestión de colecciones centrado en discos de vinilo, diseñado para ayudar a los coleccionistas a llevar un control de su stock de forma simple y centralizada. Gestiona entradas y salidas de discos, compras, ventas y usuarios.",
                        fontSize = 20.sp
                    )
                }
            }

            // Vinyl Record Section
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier .padding(horizontal = 100.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier .padding(vertical = 30.dp)
                ) {
                    Text (
                        text = "Vinilos: ",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // First row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Añadir Disco",
                            description = "Añade un disco a tu colección de vinilos.",
                            buttonText = "Añadir Disco",
                            onClick = { state.changePage( com.diskman.ui.Screen.CREATE_V) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Borrar Vinilo",
                            description = "Borrar un disco vinilo del sistema.",
                            buttonText = "Borrar disco",
                            onClick = { state.changePage( com.diskman.ui.Screen.DELETE_V) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Llistar Colección",
                            description = "Lista los discos guardados en tu colección de vinilos.",
                            buttonText = "Ver Colección",
                            onClick = { state.changePage( com.diskman.ui.Screen.SHOW_CV) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                    }
                    // Second row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 5.dp)
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Listar Discos",
                            description = "Lista los discos guardados en tu sistema.",
                            buttonText = "Ver Discos",
                            onClick = { state.changePage( com.diskman.ui.Screen.SHOW_V) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Buscar Discos",
                            description = "Busca un disco de Vinilo por todo el sistema.",
                            buttonText = "Buscar Disco",
                            onClick = { state.changePage( com.diskman.ui.Screen.FIND_V) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // User section
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier .padding(horizontal = 100.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier .padding(vertical = 30.dp)
                ) {
                    Text (
                        text = "Usuarios: ",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // First row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Añadir Usuario",
                            description = "Añadir un usuario al sistema.",
                            buttonText = "Añadir Usuario",
                            onClick = { state.changePage( com.diskman.ui.Screen.CREATE_U) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Borrar Usuario",
                            description = "Borrar un usuario del sistema.",
                            buttonText = "Borrar Usuario",
                            onClick = { state.changePage( com.diskman.ui.Screen.DELETE_U) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Llistar Usuarios",
                            description = "Lista los usuarios guardados en tu sistema.",
                            buttonText = "Ver Usuarios",
                            onClick = { state.changePage( com.diskman.ui.Screen.SHOW_U) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                    }
                    // Second row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 5.dp)
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Buscar Usuario",
                            description = "Busca un usuario por todo el sistema.",
                            buttonText = "Buscar",
                            onClick = { state.changePage( com.diskman.ui.Screen.FIND_U) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Sales section
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier .padding(horizontal = 100.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier .padding(vertical = 30.dp)
                ) {
                    Text (
                        text = "Ventas: ",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // First row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Hacer Venta",
                            description = "Añadir una venta al sistema.",
                            buttonText = "Añadir Venta",
                            onClick = { state.changePage( com.diskman.ui.Screen.CREATE_S) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Borrar Venta",
                            description = "Borrar una venta del sistema.",
                            buttonText = "Borrar Venta",
                            onClick = { state.changePage( com.diskman.ui.Screen.DELETE_S) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Llistar Ventas",
                            description = "Lista las ventas guardadas en tu sistema.",
                            buttonText = "Ver Ventas",
                            onClick = { state.changePage( com.diskman.ui.Screen.SHOW_S) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                    }
                    // Second row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 5.dp)
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Buscar Venta",
                            description = "Busca una venta por todo el sistema.",
                            buttonText = "Buscar Venta",
                            onClick = { state.changePage( com.diskman.ui.Screen.FIND_S) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Sales section
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier .padding(horizontal = 100.dp)
            ) {
                Column (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier .padding(vertical = 30.dp)
                ) {
                    Text (
                        text = "Compras: ",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // First row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Hacer Compra",
                            description = "Añadir una compra al sistema.",
                            buttonText = "Añadir Compra",
                            onClick = { state.changePage( com.diskman.ui.Screen.CREATE_P) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Borrar Compra",
                            description = "Borrar una compra del sistema.",
                            buttonText = "Borrar Compra",
                            onClick = { state.changePage( com.diskman.ui.Screen.DELETE_P) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                         com.diskman.ui.others.MenuCard(
                            title = "Llistar Compras",
                            description = "Lista las compras guardadas en tu sistema.",
                            buttonText = "Ver Compras",
                            onClick = { state.changePage( com.diskman.ui.Screen.SHOW_P) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                    }
                    // Second row
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 5.dp)
                    ) {
                         com.diskman.ui.others.MenuCard(
                            title = "Buscar Compra",
                            description = "Busca una compra por todo el sistema.",
                            buttonText = "Buscar Compra",
                            onClick = { state.changePage( com.diskman.ui.Screen.FIND_P) },
                            modifier = Modifier.width(500.dp).padding(vertical = 25.dp)
                        )
                        Spacer(
                            modifier = Modifier.width(30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier .height(80.dp) .background(color = Color(0xFF212121)) .fillMaxWidth()
            ){
                Text(
                    text = "by @pdiiazz | Kotlin & Jetpack Compose Framework | Compose Multiplatform (Desktop)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}