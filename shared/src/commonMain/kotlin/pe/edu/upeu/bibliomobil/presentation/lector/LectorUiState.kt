package pe.edu.upeu.bibliomobil.presentation.lector

import pe.edu.upeu.bibliomobil.domain.model.Lector

data class FormularioLector(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val nombreError: String? = null,
    val correoError: String? = null,
    val telefonoError: String? = null,
)

data class LectorUi(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String,
)

fun Lector.aUi(): LectorUi = LectorUi(
    id = id,
    nombre = nombre,
    correo = correo,
    telefono = telefono ?: "No registrado",
)

sealed interface LectorFase {
    data object Cargando : LectorFase
    data object SinLectores : LectorFase
    data class ConLectores(val lectores: List<LectorUi>) : LectorFase
    data class Error(val mensaje: String) : LectorFase
}

data class LectorUiState(
    val fase: LectorFase = LectorFase.SinLectores,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null,
)
