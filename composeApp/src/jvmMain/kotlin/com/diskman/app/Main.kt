package com.diskman.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.diskman.repository.JsonRepository
import com.diskman.state.InventoryState
import com.diskman.storage.InventoryDTO
import com.diskman.storage.PurchaseDTO
import com.diskman.storage.SaleDTO
import com.diskman.storage.UserDTO
import com.diskman.storage.VinylRecordDTO

/**
 * Main Method. The entry point of the application.
 * @author pdiiazz
 * */
fun main() {
    /** STATE CONNECTION */
    val state = InventoryState(
        inventoryRepository = JsonRepository("data/inventory.json", InventoryDTO.serializer()) { "inventory" },
        userRepository = JsonRepository("data/users.json", UserDTO.serializer()) { it.idUser },
        vinylRepository = JsonRepository("data/vinyl.json", VinylRecordDTO.serializer()) { it.idVinyl },
        purchaseRepository = JsonRepository("data/purchases.json", PurchaseDTO.serializer()) { it.idPurchase },
        salesRepository = JsonRepository("data/sales.json", SaleDTO.serializer()) { it.idSale }
    )

    /** REPOSITORY CONNECTION THROUGH STATE */
    state.load()

    /** MODEL CONNECTION THROUGH STATE */
    val inventory = state.inventory

    if (!inventory.userExists("admin@diskman.com")) {
        val adminUser = inventory.createUser("admin@diskman.com", "Password123", true)
        inventory.addUser(adminUser)
    }

    application {
        Window(onCloseRequest = {state.save(); exitApplication()}, title = "Diskman") {
            App(state)
        }
    }
}