package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro

class LibroRepositorioEnMemoriaTest {
    @Test
    fun asignaIdsCorrelativosDesdeUno() = runTest {
        val repository = LibroRepositorioEnMemoria()
        val primero = repository.registrar(libro("Primero"))
        val segundo = repository.registrar(libro("Segundo"))

        assertEquals(listOf(1L, 2L), listOf(primero.id, segundo.id))
    }

    @Test
    fun listaEnOrdenDeRegistro() = runTest {
        val repository = LibroRepositorioEnMemoria()
        repository.registrar(libro("Primero"))
        repository.registrar(libro("Segundo"))

        assertEquals(listOf("Primero", "Segundo"), repository.listar().map(Libro::titulo))
    }

    private fun libro(titulo: String) = Libro(0L, titulo, "Autor", 2000, 3)
}

class LectorRepositorioEnMemoriaTest {
    @Test
    fun asignaIdsCorrelativosDesdeUno() = runTest {
        val repository = LectorRepositorioEnMemoria()
        val primero = repository.registrar(lector("Ana", "ana@correo.com"))
        val segundo = repository.registrar(lector("Luis", "luis@correo.com"))

        assertEquals(listOf(1L, 2L), listOf(primero.id, segundo.id))
    }

    private fun lector(nombre: String, correo: String) = Lector(0L, nombre, correo, null)
}
