package com.diskman.storage

import kotlinx.serialization.Serializable

/**
 * Serializable data class for Sales.
 * @param idSale The ID for the Sale.
 * @param amount The amount of the Sale.
 * @param saleDate The date that you have done the sale. Format: YYYY-MM-DD.
 * @param vinylRecords The list of vinyl records. It belongs to the sale.
 * */
@Serializable
data class SaleDTO(
    val idSale: String,
    val amount: Double,
    val saleDate: String,
    val vinylRecords: List<VinylRecordDTO> = listOf()
)
