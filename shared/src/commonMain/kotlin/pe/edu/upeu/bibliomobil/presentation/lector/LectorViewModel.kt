package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    init {
        cargarLectores()
    }

    fun onNombreChange(valor: String) = actualizarFormulario {
        copy(nombre = valor, nombreError = null)
    }

    fun onCorreoChange(valor: String) = actualizarFormulario {
        copy(correo = valor, correoError = null)
    }

    fun onTelefonoChange(valor: String) = actualizarFormulario {
        copy(telefono = valor, telefonoError = null)
    }

    fun cargarLectores() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = LectorFase.Cargando) }
            listarLectores()
                .onSuccess { lectores ->
                    _uiState.update {
                        it.copy(
                            fase = if (lectores.isEmpty()) {
                                LectorFase.SinLectores
                            } else {
                                LectorFase.ConLectores(lectores.map { lector -> lector.aUi() })
                            }
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(fase = LectorFase.Error("No se pudo cargar la cartera de lectores"))
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
            registrarLector(
                nombre = formulario.nombre,
                correo = formulario.correo,
                telefono = formulario.telefono,
            ).onSuccess { lector ->
                _uiState.update {
                    it.copy(
                        formulario = FormularioLector(),
                        registrando = false,
                        mensajeExito = "Lector \"${lector.nombre}\" registrado correctamente",
                    )
                }
                cargarLectores()
            }.onFailure { error ->
                if (error is LectorInvalidoException) {
                    _uiState.update {
                        it.copy(
                            formulario = formulario.copy(
                                nombreError = error.errores.nombre,
                                correoError = error.errores.correo,
                                telefonoError = error.errores.telefono,
                            ),
                            registrando = false,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            fase = LectorFase.Error("No se pudo cargar la cartera de lectores"),
                            registrando = false,
                        )
                    }
                }
            }
        }
    }

    private fun actualizarFormulario(cambio: FormularioLector.() -> FormularioLector) {
        _uiState.update {
            it.copy(formulario = it.formulario.cambio(), mensajeExito = null)
        }
    }
}
