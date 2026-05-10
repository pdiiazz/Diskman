package com.diskman.models

/**
 * Abstract class for User. It represents a user of the system. It has inheritance.
 * @param idUser The user needs an email (ID).
 * @param password The user needs a password.
 */
abstract class User (val idUser: String, val password: String) {
    /**
     * Abstract method to give information about the user.
     * @return It returns info about the user. */
    abstract fun info(): String

    /**
     * Admin User, son of User abstract class.
     * @param idUser The user needs an email (ID).
     * @param password The user needs a password.
     * @param role The user's role. In this case is Admin
     */
    class AdminUser(idUser: String, password: String, val role: String = "Admin") : User(idUser, password) {
        override fun info(): String {
            return "$role: $idUser"
        }
    }

    /**
     * Standard User, son of User abstract class.
     *  @param idUser The user needs an email (ID).
     *  @param password The user needs a password.
     *  @param role The user's role. In this case is Standard.
     */
    class StandardUser(idUser: String, password: String, val role: String = "Standard") : User(idUser, password) {
        override fun info(): String {
            return "$role: $idUser"
        }
    }
}