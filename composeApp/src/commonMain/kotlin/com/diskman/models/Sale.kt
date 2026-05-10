package com.diskman.models

import com.diskman.exceptions.DuplicateIdException
import kotlinx.datetime.LocalDate

/**
 * Purchase Class, represents a purchase.
* @param idSale The ID for the Sale.
* @param amount The amount of the Sale.
* @param saleDate The date that you have done the sale. Format: YYYY-MM-DD.
*/
class Sale (val idSale: String, val amount: Double, val saleDate: LocalDate) {
    /** The list of vinyl records of the sale. Private setter.*/
    var vinylRecords: MutableList<VinylRecord> = mutableListOf()
        private set

    /**
    * Adds a vinyl record into this list.
    * @param vinylRecord Vinyl Record Object is needed.
    * @throws DuplicateIdException Throws this exception if there is another vinyl record with the same ID.
    */
    fun addVinylRecord(vinylRecord: VinylRecord) {
        if(vinylRecords.any() { it.idVinyl == vinylRecord.idVinyl}) throw DuplicateIdException("No puede haber 2 ID duplicados. ")
        vinylRecords.add(vinylRecord)
    }
}



