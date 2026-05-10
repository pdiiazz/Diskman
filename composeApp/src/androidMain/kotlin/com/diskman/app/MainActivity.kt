package com.diskman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.diskman.repository.AndroidJsonRepository
import com.diskman.state.InventoryState
import com.diskman.storage.InventoryDTO
import com.diskman.storage.PurchaseDTO
import com.diskman.storage.SaleDTO
import com.diskman.app.App
import com.diskman.storage.UserDTO
import com.diskman.storage.VinylRecordDTO

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val dataDir = filesDir.absolutePath

        val state = InventoryState(
            inventoryRepository = AndroidJsonRepository("$dataDir/inventory.json", InventoryDTO.serializer()) { "inventory" },
            userRepository      = AndroidJsonRepository("$dataDir/users.json", UserDTO.serializer()) { it.idUser },
            vinylRepository     = AndroidJsonRepository("$dataDir/vinyl.json", VinylRecordDTO.serializer()) { it.idVinyl },
            purchaseRepository  = AndroidJsonRepository("$dataDir/purchases.json", PurchaseDTO.serializer()) { it.idPurchase },
            salesRepository     = AndroidJsonRepository("$dataDir/sales.json", SaleDTO.serializer()) { it.idSale }
        )
        state.load()

        val inventory = state.inventory
        if (!inventory.userExists("admin@diskman.com")) {
            val adminUser = inventory.createUser("admin@diskman.com", "Password123", true)
            inventory.addUser(adminUser)
            state.save()
        }

        setContent { App(state) }
    }
}