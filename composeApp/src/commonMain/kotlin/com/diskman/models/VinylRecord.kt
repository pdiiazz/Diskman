package com.diskman.models

/**
 * Class for vinyl record. It represents a vinyl record.
 * @param idVinyl An ID for the vinyl record is needed.
 * @param vinylName A name for the vinyl record is needed.
 * @param band The band of the vinyl record is needed.
 * @param year  The release year of the vinyl record is needed.
 * @param countryEdition The edition of the vinyl record is needed.
 * @param vinylStamp The stamp of the vinyl record is needed.
 * @param gender The genre of the vinyl record is needed.
 * @param style The style of the vinyl record is needed.
 * @param category The category of the vinyl record is needed.
 * @param vinylCoverState The state of the cover of the vinyl record is needed. Format: M, NM, VG+, VG, G+, G, F, P.
 * @param diskState The state of the vinyl record is needed. Format: M, NM, VG+, VG, G+, G, F, P.
 * @param replaceVinyl If you want to replace the vinyl record in the future.
 * @param collectable If you want the vinyl record to belong to your collection, the phrase is correct.
 */
class VinylRecord (val idVinyl: String,
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
                   var replaceVinyl: Boolean,
                   var collectable: Boolean)
{
    /**
     * Method to show information about the vinyl record.
     * @return Returns the information.
     * */
    fun vinylInfo(): String {
        return "(ID: ${idVinyl}) " +
                "Álbum: $vinylName | " +
                "Banda: $band | " +
                "Año: $year | " +
                "Sello: $vinylStamp | " +
                "Edición: $countryEdition | " +
                "Género: $gender | " +
                "Estilo: $style | " +
                "Categoría: $category | " +
                "Estado Portada: $vinylCoverState | " +
                "Estado Disco: $diskState | " +
                "Para reemplazo: ${if (replaceVinyl) "Si" else "No"} | " +
                "Para la colección: ${if (collectable) "Si" else "No"}"
    }
}