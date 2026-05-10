package com.diskman.storage

import kotlinx.serialization.Serializable

/**
 * Serializable data class for vinyl Records.
 * @param idVinyl An ID for the vinyl record is needed.
*  @param vinylName A name for the vinyl record is needed.
*  @param band The band of the vinyl record is needed.
*  @param year  The release year of the vinyl record is needed.
*  @param countryEdition The edition of the vinyl record is needed.
*  @param vinylStamp The stamp of the vinyl record is needed.
*  @param gender The genre of the vinyl record is needed.
*  @param style The style of the vinyl record is needed.
*  @param category The category of the vinyl record is needed.
*  @param vinylCoverState The state of the cover of the vinyl record is needed. Format: M, NM, VG+, VG, G+, G, F, P.
*  @param diskState The state of the vinyl record is needed. Format: M, NM, VG+, VG, G+, G, F, P.
*  @param replaceVinyl If you want to replace the vinyl record in the future.
*  @param collectable If you want the vinyl record to belong to your collection, the phrase is correct.
 * */
@Serializable
data class VinylRecordDTO(
    val idVinyl: String,
    val vinylName: String,
    val band: String,
    val year: Int,
    val countryEdition: String,
    val vinylStamp: String,
    val gender: String,
    val style: String,
    val category: String,
    val vinylCoverState: String,
    val diskState: String,
    val replaceVinyl: Boolean,
    val collectable: Boolean
)

