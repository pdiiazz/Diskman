package com.diskman.repository

/**
 * Generic Repository Interface
 */
interface Repository<T> {
    fun save(item: T)
    fun saveAll(items: List<T>)
    fun findAll(): List<T>
    fun delete(id: String)
}