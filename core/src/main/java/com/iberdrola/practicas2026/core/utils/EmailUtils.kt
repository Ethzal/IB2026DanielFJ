package com.iberdrola.practicas2026.core.utils

fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9._%+-]{5,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    return email.matches(emailRegex)
}

fun getEmailError(email: String): String? {
    if (email.isEmpty()) return null

    val emailRegex = "^[A-Za-z0-9._%+-]{5,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    if (!email.matches(emailRegex)) {
        val parts = email.split("@")
        val userPart = parts[0]
        val domainPart = parts.getOrNull(1) ?: ""

        return when {
            !email.contains("@") -> "Falta el símbolo @"
            userPart.length < 5 -> "Mínimo 5 caracteres antes del @"
            domainPart.isEmpty() -> "Introduce un dominio (ej: gmail.com)"
            !domainPart.contains(".") -> "El dominio debe incluir un punto"
            domainPart.substringAfterLast(".").length < 2 -> "El dominio debe terminar en .com, .es, etc."
            else -> "Formato de email no válido"
        }
    }
    return null
}

fun String.maskEmail(): String {
    if (!this.contains("@")) return this
    val parts = this.split("@")
    val user = parts[0]
    val domain = parts[1]

    return if (user.length > 1) {
        "${user.first()}${"*".repeat(user.length - 2)}${user.last()}@$domain"
    } else {
        "*@$domain"
    }
}