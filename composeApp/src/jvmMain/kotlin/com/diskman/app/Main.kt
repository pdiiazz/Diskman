package com.diskman.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.diskman.repository.ApiRepository
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
        inventoryRepository = ApiRepository("inventory", InventoryDTO.serializer()) { "inventory" },
        userRepository = ApiRepository("users", UserDTO.serializer()) { it.idUser },
        vinylRepository = ApiRepository("vinyls", VinylRecordDTO.serializer()) { it.idVinyl },
        purchaseRepository = ApiRepository("purchases", PurchaseDTO.serializer()) { it.idPurchase },
        salesRepository = ApiRepository("sales", SaleDTO.serializer()) { it.idSale }
    )

    /** REPOSITORY CONNECTION THROUGH STATE */
    state.load()

    // Només crea l'admin si no existeix a la BD
    if (!state.userExists("admin@diskman.com")) {
        val adminUser = state.createUser("admin@diskman.com", "Password123", true)
        state.addUser(adminUser)
        state.save()
    }

    /** MODEL CONNECTION THROUGH STATE */
    val inventory = state.inventory

    application {
        Window(onCloseRequest = {state.save(); exitApplication()}, title = "Diskman") {
            App(state)
        }
    }
}