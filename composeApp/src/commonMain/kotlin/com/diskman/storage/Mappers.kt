package com.diskman.storage

import com.diskman.core.Inventory
import com.diskman.models.Purchase
import com.diskman.models.Sale
import com.diskman.models.User
import com.diskman.models.VinylRecord
import kotlinx.datetime.LocalDate

fun VinylRecord.toDTO(): VinylRecordDTO {
    return VinylRecordDTO(
        idVinyl = idVinyl,
        vinylName = vinylName,
        band = band,
        year = year,
        countryEdition = countryEdition,
        vinylStamp = vinylStamp,
        gender = gender,
        style = style,
        category = category,
        vinylCoverState = vinylCoverState,
        diskState = diskState,
        replaceVinyl = replaceVinyl,
        collectable = collectable,
    )
}

fun User.toDTO(): UserDTO {
    return when (this) {
        is User.AdminUser -> UserDTO.AdminUserDTO(
            idUser = idUser,
            password = password
        )
        is User.StandardUser -> UserDTO.StandardUserDTO(
            idUser = idUser,
            password = password
        )
        else -> throw IllegalArgumentException("Tipo de usuario desconocido")
    }
}

fun Sale.toDTO(): SaleDTO{
    return SaleDTO(
        idSale = idSale,
        amount = amount,
        saleDate = saleDate.toString(),
        vinylRecords = vinylRecords.map { it.toDTO() }
    )
}

fun Purchase.toDTO(): PurchaseDTO {
    return PurchaseDTO(
        idPurchase = idPurchase,
        amount = amount,
        purchaseDate = purchaseDate.toString(),
        vinylRecords = vinylRecords.map { it.toDTO() }
    )
}

fun Inventory.toDTO(): InventoryDTO {
    val inventoryDTO = InventoryDTO(
        idPurchase = idPurchase,
        idSale = idSale
    )

    return inventoryDTO
}

fun VinylRecordDTO.toModel(): VinylRecord {
    return VinylRecord(
        idVinyl = idVinyl,
        vinylName = vinylName,
        band = band,
        year = year,
        countryEdition = countryEdition,
        vinylStamp = vinylStamp,
        gender = gender,
        style = style,
        category = category,
        vinylCoverState = vinylCoverState,
        diskState = diskState,
        replaceVinyl = replaceVinyl,
        collectable = collectable,
    )
}

fun UserDTO.toModel(): User {
    return when (this) {
        is UserDTO.AdminUserDTO -> User.AdminUser(
            idUser = idUser,
            password = password
        )
        is UserDTO.StandardUserDTO -> User.StandardUser(
            idUser = idUser,
            password = password
        )
    }
}

fun SaleDTO.toModel(): Sale {
    val sale = Sale(
        idSale = idSale,
        amount = amount,
        saleDate = LocalDate.parse(saleDate)
    )
    vinylRecords.forEach { vinylRecord ->
        sale.addVinylRecord(vinylRecord.toModel())
    }
    return sale
}

fun PurchaseDTO.toModel(): Purchase {
    val purchase = Purchase(
        idPurchase = idPurchase,
        amount = amount,
        purchaseDate = LocalDate.parse(purchaseDate)
    )
    vinylRecords.forEach { purchase.addVinylRecord(it.toModel()) }
    return purchase
}


