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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diskman.state.InventoryState
import androidx.compose.ui.graphics.Color
import com.diskman.ui.others.DiskmanHeader

/**
 * Show Purchases Screen. It shows all the purchases of the system.
 * */
@Composable
fun ShowPurchasesScreen(state: InventoryState) {
    val purchases = state.inventory.purchases

    Column(modifier = Modifier.fillMaxSize()) {
        DiskmanHeader(state = state, "Listado de Compras")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Compras:",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(30.dp))

            if (purchases.isEmpty()) {
                Text(
                    text = "Parece que aún no hay compras por aquí...",
                    fontSize = 17.sp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(purchases) { purchase ->
                        Card(
                            modifier = Modifier
                                .width(700.dp)
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
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
    }
}

