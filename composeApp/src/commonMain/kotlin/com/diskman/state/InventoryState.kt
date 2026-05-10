package com.diskman.state

import androidx.compose.runtime.mutableStateOf
import com.diskman.core.Inventory
import com.diskman.models.Purchase
import com.diskman.models.Sale
import com.diskman.models.User
import com.diskman.models.VinylRecord
import com.diskman.repository.Repository
import com.diskman.storage.InventoryDTO
import com.diskman.storage.PurchaseDTO
import com.diskman.storage.SaleDTO
import com.diskman.storage.UserDTO
import com.diskman.storage.VinylRecordDTO
import com.diskman.storage.toDTO
import com.diskman.storage.toModel
import com.diskman.ui.*
import kotlinx.datetime.LocalDate

/**
 * Inventory State. The View Model of the project. It connects each layer.
 * */
class InventoryState(
    private val inventoryRepository: Repository<InventoryDTO>,
    private val userRepository: Repository<UserDTO>,
    private val vinylRepository: Repository<VinylRecordDTO>,
    private val purchaseRepository: Repository<PurchaseDTO>,
    private val salesRepository: Repository<SaleDTO>
) {
    /** MODEL CONNECTION **/
    var inventory = Inventory()
        private set

    fun findUser(email: String): User {
        return inventory.findUser(email.trim().lowercase())
    }

    fun userExists(email: String): Boolean {
        return inventory.userExists(email.trim().lowercase())
    }

    fun createUser(email: String, password: String, privileges: Boolean): User {
        return inventory.createUser(email.trim().lowercase(), password.trim(), privileges)
    }

    fun addUser(user: User) {
        inventory.addUser(user)
    }

    fun deleteUser(user: User) {
        inventory.deleteUser(user)
    }

    fun createPurchase(amount: Double, date: LocalDate): Purchase {
        return inventory.createPurchase(amount, date)
    }

    fun buyVinylRecords(purchase: Purchase) {
        inventory.buyVinylRecords(purchase)
    }

    fun vinylExists(idVinyl: String): Boolean {
        return inventory.catalog.any { it.idVinyl == idVinyl.trim() }
    }

    fun findPurchase(purchaseId: String): Purchase {
        return inventory.findPurchase(purchaseId.trim())
    }

    fun deletePurchase(purchase: Purchase) {
        inventory.deletePurchase(purchase)
    }

    fun createSale(amount: Double, date: LocalDate): Sale {
        return inventory.createSale(amount, date)
    }

    fun sellVinylRecords(sale: Sale) {
        inventory.sellVinylRecords(sale)
    }

    fun findSale(saleId: String): Sale {
        return inventory.findSale(saleId.trim())
    }

    fun deleteSale(sale: Sale) {
        inventory.deleteSale(sale)
    }

    fun findVinylRecord(id: String): VinylRecord {
        return inventory.findVinylRecord(id.trim())
    }

    fun createVinyl(id: String,
                    name: String,
                    band: String,
                    year: String,
                    edition: String,
                    stamp: String,
                    genre: String,
                    style: String,
                    category: String,
                    coverState: String,
                    diskState: String,
                    replace: Boolean,
                    collectable: Boolean
    ): VinylRecord {
        return inventory.createVinyl(id.trim(), name.trim(), band.trim(), year.trim().toInt(), edition.trim(), stamp.trim(), genre.trim(), style.trim(), category.trim(), coverState.trim(), diskState.trim(), replace, collectable)
    }

    fun addVinylRecord(vinyl: VinylRecord) {
        inventory.addVinylRecord(vinyl)
    }

    fun deleteVinylRecord(vinylRecord: VinylRecord) {
        inventory.deleteVinylRecord(vinylRecord)
    }

    /** UI CONNECTION **/
    // Current UI Screen
    var currentScreen = mutableStateOf(Screen.LOGIN)
        private set

    // Current User
    var currentUser = mutableStateOf<User?>(null)
        private set

    fun login(user: User, screen: Screen) {
        currentUser.value = user
        currentScreen.value = screen
    }

    fun logout() {
        currentUser.value = null
        currentScreen.value = Screen.LOGIN
    }

    // For UI
    fun changePage(screen: Screen) {
        currentScreen.value = screen
    }

    /** REPOSITORY CONNECTION */
    // Repositories for each type of object

    // For Repository
    fun save() {
        userRepository.saveAll(inventory.userlist.map { it.toDTO() })
        vinylRepository.saveAll(inventory.catalog.map { it.toDTO() })
        purchaseRepository.saveAll(inventory.purchases.map { it.toDTO() })
        salesRepository.saveAll(inventory.sales.map { it.toDTO() })
        inventoryRepository.save(inventory.toDTO())
    }

    //  For Repository
    fun load() {
        val counters = inventoryRepository.findAll().firstOrNull()
        if (counters != null) inventory.restoreCounters(counters.idPurchase, counters.idSale)

        userRepository.findAll().forEach { user ->
            inventory.addUser(user.toModel())
        }

        vinylRepository.findAll().forEach { vinyl ->
            inventory.addVinylRecord(vinyl.toModel())
        }

        salesRepository.findAll().forEach { sale ->
            inventory.loadSales(sale.toModel())
        }

        purchaseRepository.findAll().forEach { purchase ->
            inventory.loadPurchases(purchase.toModel())
        }
    }
}