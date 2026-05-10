package com.diskman.storage

import kotlinx.serialization.Serializable

/**
 * Serializable data class for Inventory (To store IDs)
 * @param idPurchase The ID for purchases
 * @param idSale The ID for sales
 * */
@Serializable
data class InventoryDTO(
    var idPurchase: Int = 0,
    var idSale: Int = 0
)
