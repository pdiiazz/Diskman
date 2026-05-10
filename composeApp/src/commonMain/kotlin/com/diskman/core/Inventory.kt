package com.diskman.core

import com.diskman.exceptions.*
import com.diskman.models.Purchase
import com.diskman.models.Sale
import com.diskman.models.User
import com.diskman.models.VinylRecord
import kotlinx.datetime.LocalDate

/**
 * Inventory Class, is the core of the application with logical operations.
 * */
class Inventory {
    /** Catalog list. It contains the different Vinyl Records objects. Private setter.  */
    var catalog: MutableList<VinylRecord> = mutableListOf()
        private set
    /** Sales list. It contains the different Sale objects. Private setter.  */
    var sales: MutableList<Sale> = mutableListOf()
        private set
    /** Purchases list. It contains the different Purchase objects. Private setter.  */
    var purchases: MutableList<Purchase> = mutableListOf()
        private set
    /** User list. It contains the different User objects. Private setter.  */
    var userlist: MutableList<User> = mutableListOf()
        private set

    /** It stores ID for Purchases. Private setter. */
    var idPurchase: Int = 0
        private set
    /** It stores ID for Sales. Private setter. */
    var idSale: Int = 0
        private set

    /**
     * Method used to create new ID for Purchases.
     * @return Returns the new ID for Purchases.
     * */
    fun purchaseIdGenerator(): String {
        idPurchase++
        return "P$idPurchase"
    }

    /**
     * Method used to create new ID for Sales.
     * @return Returns the new ID for Sales.
     * */
    fun saleIdGenerator(): String {
        idSale++
        return "S$idSale"
    }

    /**
     * Method used to load the data from the repository. It returns the data to the core of the application.
     * @param idPurchase ID for Purchases.
     * @param idSale ID for Sales.
     *
     * */
    fun restoreCounters(idPurchase: Int, idSale: Int) {
        this.idPurchase = idPurchase
        this.idSale = idSale
    }

    /**
     * Validation for Sales. It verifies if the vinyl record list of sales doesn't have repeated vinyl records.
     * @param sale The sale is needed to check if it is valid.
     * @return Returns TRUE if it's valid and FALSE if it's not.
     * */
    fun isSaleValid(sale: Sale): Boolean {
        return sale.vinylRecords.all { saleVR ->
                catalog.any { it.idVinyl == saleVR.idVinyl}
        }
    }

    /**
     * Validation for Vinyl Records. It verifies if the vinyl record already exists in the system.
     * @param vinylRecord The vinyl record is needed to check if it is valid.
     * @return Returns TRUE if it's valid and FALSE if it's not.
     * */
    fun isVinylValid(vinylRecord: VinylRecord): Boolean {
        return catalog.none {it.idVinyl == vinylRecord.idVinyl}
    }

    /**
     * Create Vinyl Record Object
     * @param id An ID for the vinyl record is needed.
     * @param name A name for the vinyl record is needed.
     * @param band The band of the vinyl record is needed.
     * @param year  The release year of the vinyl record is needed.
     * @param edition The edition of the vinyl record is needed.
     * @param stamp The stamp of the vinyl record is needed.
     * @param genre The genre of the vinyl record is needed.
     * @param style The style of the vinyl record is needed.
     * @param category The category of the vinyl record is needed.
     * @param coverState The state of the cover of the vinyl record is needed. Format: M, NM, VG+, VG, G+, G, F, P.
     * @param diskState The state of the vinyl record is needed. Format: M, NM, VG+, VG, G+, G, F, P.
     * @param isReplace If you want to replace the vinyl record in the future.
     * @param isCollectable If you want the vinyl record to belong to your collection, the phrase is correct.
     * @throws DuplicateIdException Throws this exception if already exists a vinyl record with the same ID.
     * @return Returns the Vinyl Record Object.
     * */
    fun createVinyl(id: String,
                    name: String,
                    band: String,
                    year: Int,
                    edition: String,
                    stamp: String,
                    genre: String,
                    style: String,
                    category: String,
                    coverState: String,
                    diskState: String,
                    isReplace: Boolean = false,
                    isCollectable: Boolean = false): VinylRecord
    {
        assert(id.isNotEmpty()) { "El ID no puede estar vacío" }
        if (catalog.any { it.idVinyl == id }) throw DuplicateIdException("No puede haber un disco con ID repetido.")

        val newVinyl = VinylRecord(id, name, band, year, edition, stamp, genre, style, category, coverState, diskState, isReplace, isCollectable)
        return newVinyl
    }

    /**
     * Create Purchase Object.
     * @param amount The amount of money is needed.
     * @param date The date that you made the purchase is needed. Format: YYYY-MM-DD.
     * @return Returns the Purchase Object.
     * */
    fun createPurchase(amount: Double, date: LocalDate): Purchase {
        val generatedId = purchaseIdGenerator()
        return Purchase( generatedId, amount, date)
    }

    /**
     * Create Sale Object.
     * @param amount The amount of money is needed.
     * @param date The date that you made the purchase is needed. Format: YYYY-MM-DD.
     * @return Returns the Sale Object.
     * */
    fun createSale(amount: Double, date: LocalDate): Sale {
        val newSale = Sale(saleIdGenerator(), amount, date)
        return newSale
    }

    /**
     * Add the sale to the Sales List.
     * @param sale The sale that you want to add to the system.
     * */
    fun loadSales(sale: Sale) {
        sales.add(sale)
    }

    /**
     * Create User Object.
     * @param id The ID for the User (It's an email).
     * @param password The password of this user. Format: 1 Upper Case letter, Min Length: 8 char, 1 number.
     * @param role The role of the user. If it's TRUE the User will be an Admin user, if it's FALSE the user will be a Standard User.
     * @throws DuplicateIdException Throws this exception if already exists a User with the same email.
     * @return Returns the User object.
     * */
    fun createUser(id: String, password: String, role: Boolean): User {
        if (userlist.any { it.idUser == id }) throw DuplicateIdException("No puede haber dos usuarios con el mismo ID $id")

        return if (role) {
            User.AdminUser(id, password,"Admin")
        } else {
            User.StandardUser(id, password,"Standard")
        }
    }

    /**
     * Sell Vinyl Records. It adds the sale and also remove the disks from the catalog.
     * @param sale The sale object is needed to complete the transaction.
     * @throws InvalidFormatException Throws this exception if the vinyl record list of the sale is empty.
     * */
    fun sellVinylRecords(sale: Sale) {
        if(!isSaleValid(sale))  throw InvalidSaleException("Algún disco de la venta no está en el catálogo.")

        sales.add(sale)

        val ids = sale.vinylRecords.map { it.idVinyl }.toSet()
        catalog.removeIf { it.idVinyl in ids }
    }

    /**
     * Delete a sale. It deletes a sale and also adds the vinyl records of the sale list into the catalog.
     * @param sale The sale object is needed to complete the transaction.
     */
    fun deleteSale(sale: Sale) {
        val newRecords = sale.vinylRecords.filter { saleVR -> catalog.none { it.idVinyl == saleVR.idVinyl } }

        catalog.addAll (newRecords)
        sales.removeIf { it.idSale == sale.idSale }
    }

    /**
     * Buy Vinyl Records if the record doesn't exist in the Catalog.
     * @param purchase The purchase object is needed to complete the transaction.
     */
    fun buyVinylRecords(purchase: Purchase) {
        purchases.add(purchase)
        catalog.addAll(purchase.vinylRecords)
    }

    /**
     * Adds the purchase into purchases list.
     * @param purchase The purchase object is needed to complete the transaction.
     */
    fun loadPurchases(purchase: Purchase) {
        purchases.add(purchase)
    }

    /**
     * Removes a purchase and also removes the vinyl records that you bought previously in this purchase.
     * @param purchase The purchase object is needed to complete the transaction.
     * */
    fun deletePurchase(purchase: Purchase) {
        purchases.removeIf { it.idPurchase == purchase.idPurchase }

        val ids = purchase.vinylRecords.map { it.idVinyl }.toSet()
        catalog.removeIf { it.idVinyl in ids }
    }

    /**
     * Adds a Vinyl Record into catalog.
     * @param vinyl The vinyl record object is needed.
     * @throws DuplicateIdException Throws this exception if already exists a vinyl record with the same ID.
     * */
    fun addVinylRecord(vinyl: VinylRecord) {
        if(!isVinylValid(vinyl))
            throw DuplicateIdException("Ya existe un disco con ID ${vinyl.idVinyl}")

        catalog.add(vinyl)
    }

    /**
     * Delete a Vinyl Record from the catalog. Only if it is valid.
     * @param vinyl The vinyl record object is needed.
     */
    fun deleteVinylRecord(vinyl: VinylRecord) {
        if(isVinylValid(vinyl)) return

        catalog.removeIf { it.idVinyl == vinyl.idVinyl }
    }

    /**
     * Add a User to the system.
     * @param user The user object is needed.
     * @throws DuplicateIdException Throws this exception if already exists a user with the same email.
     */
    fun addUser(user: User) {
        if (userlist.any {it.idUser == user.idUser}) throw DuplicateIdException("No puede haber un usuario con mismo ID ${user.idUser}")

        userlist.add(user)
    }

    /**
     * Delete User from the system.
     * @param user The user object is needed to remove it.
     */
    fun deleteUser(user: User) {
        userlist.removeIf {it.idUser == user.idUser}
    }

    /**
     * Find a Vinyl Disk using the ID
     * @param idVinylRecord The ID to search a vinyl record
     * @throws ElementNotFoundException Throws this exception if a vinyl record with this ID doesn't exist.
     * @return Returns the vinyl record if exists.
     *
     */
    fun findVinylRecord(idVinylRecord: String): VinylRecord {
        return catalog.find { it.idVinyl == idVinylRecord } ?: throw ElementNotFoundException("No existe ningún disco con ID $idVinylRecord")
    }

    /**
     * Find a Sale using the ID
     * @param idSale The ID to search a sale.
     * @throws ElementNotFoundException Throws this exception if a sale with this ID doesn't exist.
     * @return Returns the sale if exists.
     */
    fun findSale(idSale: String): Sale {
        return sales.find { it.idSale == idSale } ?: throw ElementNotFoundException("No existe ninguna venta con ID $idSale")
    }

    /**
     * Find a Purchase using the ID
     * @param idPurchase ID to search a purchase.
     * @throws ElementNotFoundException Throws this exception if a purchase with this ID doesn't exist.
     * @return Returns the purchase if exists.
     * */
    fun findPurchase(idPurchase: String): Purchase {
        return purchases.find { it.idPurchase == idPurchase } ?: throw ElementNotFoundException("No existe ninguna compra con ID $idPurchase")
    }

    /**
     * Find a User using the email.
     * @param idUser The ID to search a user.
     * @throws ElementNotFoundException Throws this exception if a user with this ID doesn't exist.
     * @return Returns the user if exists.
     * */
    fun findUser(idUser: String): User {
        return userlist.find { it.idUser == idUser } ?: throw ElementNotFoundException("No existe ningún usuario con este correo electrónico.")
    }

    /**
     * Checks if a user already exists in the system.
     * @param idUser The ID to search a user.
     * @return Returns TRUE if exists, FALSE if not.
     * */
    fun userExists(idUser: String): Boolean {
        return userlist.any { it.idUser == idUser }
    }
}