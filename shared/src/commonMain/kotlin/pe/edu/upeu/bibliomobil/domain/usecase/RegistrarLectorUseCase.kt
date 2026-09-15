package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
) {
    val tieneErrores: Boolean
        get() = nombre != null || correo != null || telefono != null
}

class LectorInvalidoException(
    val errores: ErroresDeLector,
) : IllegalArgumentException("Los datos del lector no son válidos")

class RegistrarLectorUseCase(
    private val repository: LectorRepository,
) {
    fun validar(nombre: String, correo: String, telefono: String): ErroresDeLector {
        val telefonoLimpio = telefono.trim()

        return ErroresDeLector(
            nombre = if (nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = when {
                correo.isBlank() -> "El correo es obligatorio"
                !CORREO_REGEX.matches(correo.trim()) -> "El correo no tiene un formato válido"
                else -> null
            },
            telefono = when {
                telefonoLimpio.isEmpty() -> null
                !telefonoLimpio.all(Char::isDigit) || telefonoLimpio.length !in 6..9 ->
                    "El teléfono debe tener entre 6 y 9 dígitos"
                else -> null
            },
        )
    }

    suspend operator fun invoke(nombre: String, correo: String, telefono: String): Result<Lector> {
        val errores = validar(nombre, correo, telefono)
        if (errores.tieneErrores) return Result.failure(LectorInvalidoException(errores))

        return resultadoDe {
            repository.registrar(
                Lector(
                    id = 0L,
                    nombre = nombre.trim(),
                    correo = correo.trim(),
                    telefono = telefono.trim().ifBlank { null },
                )
            )
        }
    }

    private companion object {
        val CORREO_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
