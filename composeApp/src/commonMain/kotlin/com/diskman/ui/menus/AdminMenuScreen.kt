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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
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

    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(LocalDensity.current) { windowInfo.containerSize.width.toDp() }
    val isMobile = screenWidth < 800.dp

    val logoSize = if (isMobile) 40.dp else 100.dp
    val titleSize = if (isMobile) 22.sp else 40.sp
    val subtitleSize = if (isMobile) 13.sp else 24.sp
    val sectionSize = if (isMobile) 20.sp else 30.sp
    val hPad = if (isMobile) 16.dp else 100.dp

    Column(modifier = Modifier.fillMaxSize()) {

        // Header
        if (isMobile) {
            Column(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF141A66))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(painter = painterResource(Res.drawable.small), contentDescription = "Logo", modifier = Modifier.size(logoSize))
                    Column {
                        Text("Diskman", color = Color.White, fontWeight = FontWeight.Bold, fontSize = titleSize)
                        Text("Panel de administración", color = Color(0xFFB0BEC5), fontSize = subtitleSize)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(user?.idUser ?: "?", color = Color.White, fontSize = 12.sp)
                        Text(if (user is User.AdminUser) "Administrador" else "?", color = Color(0xFFB0BEC5), fontSize = 11.sp)
                    }
                    Button(onClick = { state.logout() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF546E7A)), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("Salir", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF141A66)).padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Image(painter = painterResource(Res.drawable.small), contentDescription = "Logo", modifier = Modifier.size(logoSize))
                    Column {
                        Text("Diskman", color = Color.White, fontWeight = FontWeight.Bold, fontSize = titleSize)
                        Text("Panel de administración", color = Color(0xFFB0BEC5), fontSize = subtitleSize)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(user?.idUser ?: "?", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 20.sp)
                        Text(if (user is User.AdminUser) "Administrador" else "?", color = Color(0xFFB0BEC5), fontSize = 17.sp)
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Button(onClick = { state.logout() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF546E7A)), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(horizontal = 26.dp, vertical = 8.dp)) {
                        Text("Cerrar sesión", fontSize = 13.sp, color = Color.White)
                    }
                }
            }
        }

        // Content
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = hPad),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(45.dp))

            Text("¿Qué es Diskman?", fontSize = titleSize, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Diskman es un programa de gestión de colecciones centrado en discos de vinilo, diseñado para ayudar a los coleccionistas a llevar un control de su stock de forma simple y centralizada.", fontSize = if (isMobile) 14.sp else 20.sp)

            Spacer(modifier = Modifier.height(45.dp))

            // Vinyl Records
            Text("Vinilos:", fontSize = sectionSize, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (isMobile) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    com.diskman.ui.others.MenuCard("Añadir Disco", "Añade un disco a tu colección.", "Añadir Disco", { state.changePage(com.diskman.ui.Screen.CREATE_V) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Borrar Vinilo", "Borrar un disco del sistema.", "Borrar disco", { state.changePage(com.diskman.ui.Screen.DELETE_V) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Listar Colección", "Lista los discos de tu colección.", "Ver Colección", { state.changePage(com.diskman.ui.Screen.SHOW_CV) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Listar Discos", "Lista los discos del sistema.", "Ver Discos", { state.changePage(com.diskman.ui.Screen.SHOW_V) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Buscar Discos", "Busca un disco por el sistema.", "Buscar Disco", { state.changePage(com.diskman.ui.Screen.FIND_V) }, Modifier.fillMaxWidth())
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    com.diskman.ui.others.MenuCard("Añadir Disco", "Añade un disco a tu colección.", "Añadir Disco", { state.changePage(com.diskman.ui.Screen.CREATE_V) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Borrar Vinilo", "Borrar un disco del sistema.", "Borrar disco", { state.changePage(com.diskman.ui.Screen.DELETE_V) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Listar Colección", "Lista los discos de tu colección.", "Ver Colección", { state.changePage(com.diskman.ui.Screen.SHOW_CV) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Listar Discos", "Lista los discos del sistema.", "Ver Discos", { state.changePage(com.diskman.ui.Screen.SHOW_V) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Buscar Discos", "Busca un disco por el sistema.", "Buscar Disco", { state.changePage(com.diskman.ui.Screen.FIND_V) }, Modifier.width(300.dp))
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            // Users
            Text("Usuarios:", fontSize = sectionSize, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (isMobile) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    com.diskman.ui.others.MenuCard("Añadir Usuario", "Añadir un usuario al sistema.", "Añadir Usuario", { state.changePage(com.diskman.ui.Screen.CREATE_U) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Borrar Usuario", "Borrar un usuario del sistema.", "Borrar Usuario", { state.changePage(com.diskman.ui.Screen.DELETE_U) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Listar Usuarios", "Lista los usuarios del sistema.", "Ver Usuarios", { state.changePage(com.diskman.ui.Screen.SHOW_U) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Buscar Usuario", "Busca un usuario por el sistema.", "Buscar", { state.changePage(com.diskman.ui.Screen.FIND_U) }, Modifier.fillMaxWidth())
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    com.diskman.ui.others.MenuCard("Añadir Usuario", "Añadir un usuario al sistema.", "Añadir Usuario", { state.changePage(com.diskman.ui.Screen.CREATE_U) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Borrar Usuario", "Borrar un usuario del sistema.", "Borrar Usuario", { state.changePage(com.diskman.ui.Screen.DELETE_U) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Listar Usuarios", "Lista los usuarios del sistema.", "Ver Usuarios", { state.changePage(com.diskman.ui.Screen.SHOW_U) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Buscar Usuario", "Busca un usuario por el sistema.", "Buscar", { state.changePage(com.diskman.ui.Screen.FIND_U) }, Modifier.width(300.dp))
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            // Sales
            Text("Ventas:", fontSize = sectionSize, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (isMobile) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    com.diskman.ui.others.MenuCard("Hacer Venta", "Añadir una venta al sistema.", "Añadir Venta", { state.changePage(com.diskman.ui.Screen.CREATE_S) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Borrar Venta", "Borrar una venta del sistema.", "Borrar Venta", { state.changePage(com.diskman.ui.Screen.DELETE_S) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Listar Ventas", "Lista las ventas del sistema.", "Ver Ventas", { state.changePage(com.diskman.ui.Screen.SHOW_S) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Buscar Venta", "Busca una venta por el sistema.", "Buscar Venta", { state.changePage(com.diskman.ui.Screen.FIND_S) }, Modifier.fillMaxWidth())
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    com.diskman.ui.others.MenuCard("Hacer Venta", "Añadir una venta al sistema.", "Añadir Venta", { state.changePage(com.diskman.ui.Screen.CREATE_S) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Borrar Venta", "Borrar una venta del sistema.", "Borrar Venta", { state.changePage(com.diskman.ui.Screen.DELETE_S) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Listar Ventas", "Lista las ventas del sistema.", "Ver Ventas", { state.changePage(com.diskman.ui.Screen.SHOW_S) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Buscar Venta", "Busca una venta por el sistema.", "Buscar Venta", { state.changePage(com.diskman.ui.Screen.FIND_S) }, Modifier.width(300.dp))
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            // Purchases
            Text("Compras:", fontSize = sectionSize, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (isMobile) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    com.diskman.ui.others.MenuCard("Hacer Compra", "Añadir una compra al sistema.", "Añadir Compra", { state.changePage(com.diskman.ui.Screen.CREATE_P) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Borrar Compra", "Borrar una compra del sistema.", "Borrar Compra", { state.changePage(com.diskman.ui.Screen.DELETE_P) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Listar Compras", "Lista las compras del sistema.", "Ver Compras", { state.changePage(com.diskman.ui.Screen.SHOW_P) }, Modifier.fillMaxWidth())
                    com.diskman.ui.others.MenuCard("Buscar Compra", "Busca una compra por el sistema.", "Buscar Compra", { state.changePage(com.diskman.ui.Screen.FIND_P) }, Modifier.fillMaxWidth())
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    com.diskman.ui.others.MenuCard("Hacer Compra", "Añadir una compra al sistema.", "Añadir Compra", { state.changePage(com.diskman.ui.Screen.CREATE_P) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Borrar Compra", "Borrar una compra del sistema.", "Borrar Compra", { state.changePage(com.diskman.ui.Screen.DELETE_P) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Listar Compras", "Lista las compras del sistema.", "Ver Compras", { state.changePage(com.diskman.ui.Screen.SHOW_P) }, Modifier.width(300.dp))
                    com.diskman.ui.others.MenuCard("Buscar Compra", "Busca una compra por el sistema.", "Buscar Compra", { state.changePage(com.diskman.ui.Screen.FIND_P) }, Modifier.width(300.dp))
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}