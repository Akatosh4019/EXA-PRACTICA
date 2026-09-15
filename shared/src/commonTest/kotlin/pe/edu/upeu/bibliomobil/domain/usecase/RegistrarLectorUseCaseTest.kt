package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import pe.edu.upeu.bibliomobil.testutil.FakeLectorRepository

class RegistrarLectorUseCaseTest {
    @Test
    fun rechazaCorreoInvalido() = runTest {
        val resultado = useCase()("Ana", "correo-invalido", "987654321")
        val error = resultado.exceptionOrNull() as LectorInvalidoException
        assertEquals("El correo no tiene un formato válido", error.errores.correo)
    }

    @Test
    fun rechazaTelefonoCorto() = runTest {
        val resultado = useCase()("Ana", "ana@correo.com", "12345")
        val error = resultado.exceptionOrNull() as LectorInvalidoException
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", error.errores.telefono)
    }

    @Test
    fun guardaTelefonoEnBlancoComoNull() = runTest {
        val lector = useCase()("Ana", "ana@correo.com", "   ").getOrThrow()
        assertNull(lector.telefono)
    }

    @Test
    fun propagaFalloDelRepositorio() = runTest {
        val repository = FakeLectorRepository().apply { fallarAlRegistrar = true }
        val resultado = RegistrarLectorUseCase(repository)("Ana", "ana@correo.com", "987654321")
        assertTrue(resultado.isFailure)
    }

    private fun useCase() = RegistrarLectorUseCase(FakeLectorRepository())
}
