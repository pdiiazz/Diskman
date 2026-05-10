package com.diskman.storage

import kotlinx.serialization.Serializable

/**
 * Serializable data class for Purchases.
 * @param idPurchase The ID for the Purchase.
 * @param amount The amount of the Purchase.
 * @param purchaseDate The date that you have done the purchase. Format: YYYY-MM-DD.
 * @param vinylRecords The list of vinyl records. It belongs to the purchase.
 * */
@Serializable
data class PurchaseDTO(
    val idPurchase: String,
    val amount: Double,
    val purchaseDate: String,
    val vinylRecords: List<VinylRecordDTO> = listOf()
)