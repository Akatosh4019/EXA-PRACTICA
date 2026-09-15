package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibroTest {
    @Test
    fun rechazaTituloVacio() {
        assertFailsWith<IllegalArgumentException> { libro(titulo = "   ") }
    }

    @Test
    fun rechazaAnioFueraDeRango() {
        assertFailsWith<IllegalArgumentException> { libro(anio = 1449) }
    }

    @Test
    fun requiereReposicionConDosEjemplares() {
        assertTrue(libro(ejemplares = 2).requiereReposicion)
    }

    @Test
    fun noRequiereReposicionConTresEjemplares() {
        assertFalse(libro(ejemplares = 3).requiereReposicion)
    }
}

class DetallePrestamoTest {
    @Test
    fun rechazaCeroDias() {
        assertFailsWith<IllegalArgumentException> { DetallePrestamo(libro(), 0) }
    }

    @Test
    fun rechazaDieciseisDias() {
        assertFailsWith<IllegalArgumentException> { DetallePrestamo(libro(), 16) }
    }

    @Test
    fun calculaMultaDeCuatroDias() {
        assertEquals(6.0, DetallePrestamo(libro(), 5).multaPorRetraso(4))
    }
}

private fun libro(
    titulo: String = "Cien años de soledad",
    anio: Int = 1967,
    ejemplares: Int = 3,
) = Libro(
    id = 1L,
    titulo = titulo,
    autor = "Gabriel García Márquez",
    anio = anio,
    ejemplares = ejemplares,
)
