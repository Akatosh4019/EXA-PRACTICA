package pe.edu.upeu.bibliomobil.presentation.libro

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.testutil.FakeLibroRepository

@OptIn(ExperimentalCoroutinesApi::class)
class LibroViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun arrancaEnSinLibros() {
        assertIs<LibroFase.SinLibros>(viewModel(FakeLibroRepository()).uiState.value.fase)
    }

    @Test
    fun muestraLineaSecundariaConFormatoExacto() {
        val repository = FakeLibroRepository(listOf(Libro(1L, "Libro", "Autor", 1998, 3)))
        val fase = viewModel(repository).uiState.value.fase as LibroFase.ConLibros

        assertEquals("1998 · 3 ejemplares", fase.libros.single().lineaSecundaria)
    }

    @Test
    fun pasaAErrorSiElRepositorioFalla() {
        val repository = FakeLibroRepository().apply { fallarAlListar = true }
        val fase = viewModel(repository).uiState.value.fase

        assertEquals("No se pudo cargar el catálogo", (fase as LibroFase.Error).mensaje)
    }

    @Test
    fun erroresDeValidacionCaenEnFormularioNoEnFase() = runTest {
        val viewModel = viewModel(FakeLibroRepository())
        viewModel.registrar()

        val estado = viewModel.uiState.value
        assertEquals("El título es obligatorio", estado.formulario.tituloError)
        assertIs<LibroFase.SinLibros>(estado.fase)
    }

    @Test
    fun registrarLimpiaFormularioYRecargaCatalogo() = runTest {
        val repository = FakeLibroRepository()
        val viewModel = viewModel(repository)
        viewModel.onTituloChange("Rayuela")
        viewModel.onAutorChange("Julio Cortázar")
        viewModel.onAnioChange("1963")
        viewModel.onEjemplaresChange("4")
        viewModel.registrar()

        val estado = viewModel.uiState.value
        assertEquals(FormularioLibro(), estado.formulario)
        assertIs<LibroFase.ConLibros>(estado.fase)
        assertEquals(1, estado.fase.libros.size)
    }

    @Test
    fun dobleRegistroInmediatoNoDuplicaLibro() = runTest(dispatcher) {
        val bloqueo = CompletableDeferred<Unit>()
        val repository = FakeLibroRepository().apply { bloqueoRegistro = bloqueo }
        val viewModel = viewModel(repository)
        viewModel.onTituloChange("Rayuela")
        viewModel.onAutorChange("Julio Cortázar")
        viewModel.onAnioChange("1963")
        viewModel.onEjemplaresChange("4")

        viewModel.registrar()
        viewModel.registrar()
        bloqueo.complete(Unit)

        assertEquals(1, repository.registrosRealizados)
    }

    private fun viewModel(repository: FakeLibroRepository) = LibroViewModel(
        registrarLibro = RegistrarLibroUseCase(repository),
        listarLibros = ListarLibrosUseCase(repository),
    )
}
