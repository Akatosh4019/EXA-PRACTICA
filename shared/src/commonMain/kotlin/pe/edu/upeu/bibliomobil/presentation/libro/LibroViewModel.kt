package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun onTituloChange(valor: String) = actualizarFormulario {
        copy(titulo = valor, tituloError = null)
    }

    fun onAutorChange(valor: String) = actualizarFormulario {
        copy(autor = valor, autorError = null)
    }

    fun onAnioChange(valor: String) = actualizarFormulario {
        copy(anio = valor, anioError = null)
    }

    fun onEjemplaresChange(valor: String) = actualizarFormulario {
        copy(ejemplares = valor, ejemplaresError = null)
    }

    fun cargarLibros() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = LibroFase.Cargando) }
            listarLibros()
                .onSuccess { libros ->
                    _uiState.update {
                        it.copy(
                            fase = if (libros.isEmpty()) {
                                LibroFase.SinLibros
                            } else {
                                LibroFase.ConLibros(libros.map { it.aUi() })
                            }
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(fase = LibroFase.Error("No se pudo cargar el catálogo"))
                    }
                }
        }
    }

    fun registrar() {
        val estado = _uiState.value
        if (estado.registrando) return

        val formulario = estado.formulario
        _uiState.update { it.copy(registrando = true, mensajeExito = null) }

        viewModelScope.launch {
            registrarLibro(
                titulo = formulario.titulo,
                autor = formulario.autor,
                anio = formulario.anio,
                ejemplares = formulario.ejemplares,
            ).onSuccess { libro ->
                _uiState.update {
                    it.copy(
                        formulario = FormularioLibro(),
                        registrando = false,
                        mensajeExito = "Libro \"${libro.titulo}\" registrado correctamente",
                    )
                }
                cargarLibros()
            }.onFailure { error ->
                if (error is LibroInvalidoException) {
                    _uiState.update {
                        it.copy(
                            formulario = formulario.copy(
                                tituloError = error.errores.titulo,
                                autorError = error.errores.autor,
                                anioError = error.errores.anio,
                                ejemplaresError = error.errores.ejemplares,
                            ),
                            registrando = false,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            fase = LibroFase.Error("No se pudo cargar el catálogo"),
                            registrando = false,
                        )
                    }
                }
            }
        }
    }

    private fun actualizarFormulario(cambio: FormularioLibro.() -> FormularioLibro) {
        _uiState.update {
            it.copy(formulario = it.formulario.cambio(), mensajeExito = null)
        }
    }
}
