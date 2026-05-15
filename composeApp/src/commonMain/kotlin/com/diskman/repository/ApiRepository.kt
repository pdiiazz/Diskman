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

class ApiRepository<T>(
    private val endpoint: String,
    private val serializer: KSerializer<T>,
    private val getId: (T) -> String
) : Repository<T> {

    private val baseUrl = "http://localhost:3000"
    private val json = Json { ignoreUnknownKeys = true }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    override fun findAll(): List<T> {
        return runCatching {
            runBlocking {
                val response = client.get("$baseUrl/$endpoint")
                val text = response.body<String>()
                json.decodeFromString(ListSerializer(serializer), text)
            }
        }.getOrDefault(emptyList())
    }

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

    override fun saveAll(items: List<T>) {
        items.forEach { save(it) }
    }

    override fun delete(id: String) {
        runCatching {
            runBlocking {
                client.delete("$baseUrl/$endpoint/$id")
            }
        }
    }
}