package com.diskman.repository

import com.diskman.exceptions.WritingFailException
import com.diskman.exceptions.WrongFileFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

/**
 * JSON Repository to store and load data
 * @param fileName The file Name that contains data. Private variable.
 * @param serializer The serializer used to serialize data. In this case I used Kserializer. Private variable.
 * @param getId
 */
class JsonRepository<T>(
    private val fileName: String,
    private val serializer: KSerializer<T>,
    private val getId: (T) -> String
) : Repository <T> {
    /** Pretty Print activated */
    private val json = Json { prettyPrint = true }

    /**
     * Reads data from the File.
     * @return Returns a list of objects <T>.
     */
    private fun readFromFile(): MutableList<T> {
        val file = File(fileName)

        if (!file.exists()) return mutableListOf()

        val content = file.readText()
        if (content.isBlank()) return mutableListOf()

        return try {
            json.decodeFromString(
                ListSerializer(serializer),
                content
            ).toMutableList()
        } catch (e: Exception) {
            throw WrongFileFormat("Archivo de datos $fileName no encontrado. ${e.message}")
        }
    }

    /**
     * Write data into a file
     * @param items It needs a list of objects <T> to serialize them.
     * */
    private fun writeToFile(items: List<T>) {
        val file = File(fileName)
        file.parentFile?.mkdirs()

        try {
            val jsonString = json.encodeToString(
                ListSerializer(serializer),
                items
            )

            file.writeText(jsonString)
        } catch (e: Exception) {
            throw WritingFailException("Error en la escritura del archivo $fileName. ${e.message}")
        }
    }
    /**
     * Save all the items in the file
     * @param items List of objects <T>
     */
    override fun saveAll(items: List<T>) {
        writeToFile(items)
    }

    /**
     * Method based in save() method of the Repository Interface. It allows you to save data.
     * @param item The type of data you want to storage.
     * */
    override fun save(item: T) {
        val items = readFromFile()

        val index = items.indexOfFirst { getId(it) == getId(item) }
        if (index >= 0) {
            items[index] = item
        } else {
            items.add(item)
        }
        writeToFile(items)
    }

    /**
     * Method based in findAll() method of the Repository Interface. It allows you to take the entire list of objects.
     * @return Returns a List of objects.
     * */
    override fun findAll(): List<T> {
        return readFromFile()
    }

    /**
     * Method based in delete() method of the Repository Interface. It allows you to delete objects.
     * @param id It needs the id of the object to remove it.
     */
    override fun delete(id: String) {
        val items = readFromFile()

        val filtered = items.filterNot { getId(it) == id }
        writeToFile(filtered)
    }
}