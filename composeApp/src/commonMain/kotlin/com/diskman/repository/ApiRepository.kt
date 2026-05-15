package com.diskman.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * Generic REST repository implementation using Ktor Client.
 *
 * @param T Entity type.
 * @param endpoint API endpoint path.
 * @param serializer Serializer for JSON conversion.
 * @param getId Function to extract entity ID.
 */
class ApiRepository<T>(
    private val endpoint: String,
    private val serializer: KSerializer<T>,
    private val getId: (T) -> String
) : Repository<T> {
    /** Base API URL. It's a Local API  */
    private val baseUrl = "http://localhost:3000"
    /** JSON configuration. */
    private val json = Json { ignoreUnknownKeys = true }

    /** HTTP client instance. */
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    /**
     * Retrieves all entities from the API.
     *
     * @return List of entities or empty list on failure.
     */
    override fun findAll(): List<T> {
        return runCatching {
            runBlocking {
                val response = client.get("$baseUrl/$endpoint")
                val text = response.body<String>()
                json.decodeFromString(ListSerializer(serializer), text)
            }
        }.getOrDefault(emptyList())
    }

    /**
     * Saves a single entity.
     *
     * @param item Entity to save.
     */
    override fun save(item: T) {
        runCatching {
            runBlocking {
                val body = json.encodeToString(serializer, item)
                client.post("$baseUrl/$endpoint") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }
        }
    }

    /**
     * Saves multiple entities.
     *
     * @param items Entities to save.
     */
    override fun saveAll(items: List<T>) {
        items.forEach { save(it) }
    }

    /**
     * Deletes an entity by ID.
     *
     * @param id Entity identifier.
     */
    override fun delete(id: String) {
        runCatching {
            runBlocking {
                client.delete("$baseUrl/$endpoint/$id")
            }
        }
    }
}