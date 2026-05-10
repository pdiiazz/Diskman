package com.diskman.repository

import com.diskman.exceptions.WritingFailException
import com.diskman.exceptions.WrongFileFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

class AndroidJsonRepository<T>(
    private val fileName: String,
    private val serializer: KSerializer<T>,
    private val getId: (T) -> String
) : Repository<T> {

    private val json = Json { prettyPrint = true }

    private fun readFromFile(): MutableList<T> {
        val file = File(fileName)
        if (!file.exists()) return mutableListOf()
        val content = file.readText()
        if (content.isBlank()) return mutableListOf()
        return try {
            json.decodeFromString(ListSerializer(serializer), content).toMutableList()
        } catch (e: Exception) {
            throw WrongFileFormat("Archivo $fileName con formato incorrecto. ${e.message}")
        }
    }

    private fun writeToFile(items: List<T>) {
        val file = File(fileName)
        file.parentFile?.mkdirs()
        try {
            file.writeText(json.encodeToString(ListSerializer(serializer), items))
        } catch (e: Exception) {
            throw WritingFailException("Error escribiendo $fileName. ${e.message}")
        }
    }

    override fun saveAll(items: List<T>) = writeToFile(items)

    override fun save(item: T) {
        val items = readFromFile()
        val index = items.indexOfFirst { getId(it) == getId(item) }
        if (index >= 0) items[index] = item else items.add(item)
        writeToFile(items)
    }

    override fun findAll(): List<T> = readFromFile()

    override fun delete(id: String) {
        writeToFile(readFromFile().filterNot { getId(it) == id })
    }
}