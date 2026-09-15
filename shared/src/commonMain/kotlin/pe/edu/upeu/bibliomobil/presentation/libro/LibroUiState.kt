package pe.edu.upeu.bibliomobil.presentation.libro

import pe.edu.upeu.bibliomobil.domain.model.Libro

data class FormularioLibro(
    val titulo: String = "",
    val autor: String = "",
    val anio: String = "",
    val ejemplares: String = "",
    val tituloError: String? = null,
    val autorError: String? = null,
    val anioError: String? = null,
    val ejemplaresError: String? = null,
)

data class LibroUi(
    val id: Long,
    val titulo: String,
    val autor: String,
    val lineaSecundaria: String,
    val requiereReposicion: Boolean,
)

fun Libro.aUi(): LibroUi = LibroUi(
    id = id,
    titulo = titulo,
    autor = autor,
    lineaSecundaria = "$anio · $ejemplares ${if (ejemplares == 1) "ejemplar" else "ejemplares"}",
    requiereReposicion = requiereReposicion,
)

sealed interface LibroFase {
    data object Cargando : LibroFase
    data object SinLibros : LibroFase
    data class ConLibros(val libros: List<LibroUi>) : LibroFase
    data class Error(val mensaje: String) : LibroFase
}

data class LibroUiState(
    val fase: LibroFase = LibroFase.SinLibros,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null,
)
