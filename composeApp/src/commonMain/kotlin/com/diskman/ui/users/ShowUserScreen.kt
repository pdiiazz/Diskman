package com.diskman.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.state.InventoryState
import com.diskman.ui.others.DiskmanHeader

/**
 * Show User Screen. It shows all the users of the system.
 * */
@Composable
fun ShowUserScreen(state: InventoryState) {
    val userlist = state.inventory.userlist

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state = state, "Usuarios del sistema")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Usuarios:",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
            )
            Spacer(modifier = Modifier.height(30.dp))

            if (userlist.isEmpty()) {
                Text(
                    text = "Parece que aún no hay usuarios por aquí...",
                    fontSize = 17.sp
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    items(userlist) { user ->
                        Column(
                            modifier = Modifier.background(color = Color(0xFFEEEEEE)).padding(10.dp)
                        ) {
                            Text(
                                text = user.info(),
                                fontSize = 17.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}