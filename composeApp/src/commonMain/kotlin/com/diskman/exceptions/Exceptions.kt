package com.diskman.exceptions

/**
 * Duplicate ID Exception
 * @param message Custom message for the exception.
 * */
class DuplicateIdException(message: String) : Exception(message)

/**
 * Element Not Found Exception
 *  @param message Custom message for the exception.
 */
class ElementNotFoundException(message: String) : Exception(message)

/**
 * Invalid format for password or email
 * @param message Custom message for the exception.
 */
class InvalidFormatException(message: String) : Exception(message)

/** If the passwords are not the same
 * @param message Custom message for the exception.
 */
class PasswordMismatchException(message: String) : Exception(message)

/** When the user has an active session
 * @param message Custom message for the exception.
 */
class ActiveSessionException(message: String) : Exception(message)

/** If the sale is not valid
 * @param message Custom message for the exception.
 */
class InvalidSaleException(message: String) : Exception(message)

/** If JSON has not a good format
 *  @param message Custom message for the exception.
 */
class WrongFileFormat(message: String) : Exception(message)

/** If it fails while it's writing the data into the file
 *  @param message Custom message for the exception.
 */
class WritingFailException(message: String) : Exception(message)