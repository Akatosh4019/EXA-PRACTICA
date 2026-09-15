package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import pe.edu.upeu.bibliomobil.testutil.FakeLibroRepository

class RegistrarLibroUseCaseTest {
    @Test
    fun aceptaLibroValidoYRecortaTextos() = runTest {
        val resultado = RegistrarLibroUseCase(FakeLibroRepository())(
            titulo = "  Rayuela  ", autor = "  Julio Cortázar  ", anio = "1963", ejemplares = "4"
        ).getOrThrow()

        assertEquals("Rayuela", resultado.titulo)
        assertEquals("Julio Cortázar", resultado.autor)
    }

    @Test
    fun informaTituloObligatorio() = assertEquals(
        "El título es obligatorio",
        useCase().validar(" ", "Autor", "2000", "1").titulo,
    )

    @Test
    fun informaAutorObligatorio() = assertEquals(
        "El autor es obligatorio",
        useCase().validar("Libro", " ", "2000", "1").autor,
    )

    @Test
    fun informaAnioObligatorio() = assertEquals(
        "El año es obligatorio",
        useCase().validar("Libro", "Autor", "", "1").anio,
    )

    @Test
    fun informaAnioNoEntero() = assertEquals(
        "El año debe ser un número entero",
        useCase().validar("Libro", "Autor", "dos mil", "1").anio,
    )

    @Test
    fun informaAnioFueraDeRango() = assertEquals(
        "El año debe estar entre 1450 y 2026",
        useCase().validar("Libro", "Autor", "1400", "1").anio,
    )

    @Test
    fun informaEjemplaresObligatorios() = assertEquals(
        "Los ejemplares son obligatorios",
        useCase().validar("Libro", "Autor", "2000", "").ejemplares,
    )

    @Test
    fun informaEjemplaresNoEnteros() = assertEquals(
        "Los ejemplares deben ser un número entero",
        useCase().validar("Libro", "Autor", "2000", "varios").ejemplares,
    )

    @Test
    fun informaEjemplaresNegativos() = assertEquals(
        "Los ejemplares no pueden ser negativos",
        useCase().validar("Libro", "Autor", "2000", "-1").ejemplares,
    )

    @Test
    fun enviaIdCeroYRepositorioAsignaId() = runTest {
        val repository = FakeLibroRepository()
        val resultado = RegistrarLibroUseCase(repository)("Libro", "Autor", "2000", "1").getOrThrow()
        assertEquals(0L, repository.ultimoLibroRecibido?.id)
        assertEquals(1L, resultado.id)
    }

    @Test
    fun propagaFalloDelRepositorioComoResultFailure() = runTest {
        val repository = FakeLibroRepository().apply { fallarAlRegistrar = true }
        val resultado = RegistrarLibroUseCase(repository)("Libro", "Autor", "2000", "1")

        assertTrue(resultado.isFailure)
        assertIs<IllegalStateException>(resultado.exceptionOrNull())
    }

    private fun useCase() = RegistrarLibroUseCase(FakeLibroRepository())
}
