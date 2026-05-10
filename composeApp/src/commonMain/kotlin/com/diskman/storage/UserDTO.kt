package com.diskman.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable sealed class for Users. (Admin/Standard)
 * */
@Serializable
sealed class UserDTO {
    abstract val idUser: String
    abstract val password: String

    /**
     * Serializable data class for Admin Users.
     * @param idUser The user needs an email (ID).
     * @param password The user needs a password.
     * @return Returns a User DTO.
     * */
    @Serializable
    @SerialName("Admin")
    data class AdminUserDTO(
        override val idUser: String,
        override val password: String
    ) : UserDTO()

    /**
     * Serializable data class for Standard Users.
     * @param idUser The user needs an email (ID).
     * @param password The user needs a password.
     * @return Returns a User DTO.
     * */
    @Serializable
    @SerialName("Standard")
    data class StandardUserDTO(
        override val idUser: String,
        override val password: String
    ) : UserDTO()
}