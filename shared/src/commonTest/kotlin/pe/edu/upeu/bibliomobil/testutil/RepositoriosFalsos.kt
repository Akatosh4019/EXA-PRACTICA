package pe.edu.upeu.bibliomobil.testutil

import kotlinx.coroutines.CompletableDeferred
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository(
    librosIniciales: List<Libro> = emptyList(),
) : LibroRepository {
    private val libros = librosIniciales.toMutableList()
    var fallarAlRegistrar = false
    var fallarAlListar = false
    var bloqueoRegistro: CompletableDeferred<Unit>? = null
    var registrosRealizados = 0
        private set
    var ultimoLibroRecibido: Libro? = null
        private set

    override suspend fun registrar(libro: Libro): Libro {
        if (fallarAlRegistrar) error("Fallo simulado al registrar libro")
        bloqueoRegistro?.await()
        ultimoLibroRecibido = libro
        registrosRealizados++
        return libro.copy(id = (libros.maxOfOrNull(Libro::id) ?: 0L) + 1L)
            .also(libros::add)
    }

    override suspend fun listar(): List<Libro> {
        if (fallarAlListar) error("Fallo simulado al listar libros")
        return libros.toList()
    }
}

class FakeLectorRepository(
    lectoresIniciales: List<Lector> = emptyList(),
) : LectorRepository {
    private val lectores = lectoresIniciales.toMutableList()
    var fallarAlRegistrar = false
    var fallarAlListar = false

    override suspend fun registrar(lector: Lector): Lector {
        if (fallarAlRegistrar) error("Fallo simulado al registrar lector")
        return lector.copy(id = (lectores.maxOfOrNull(Lector::id) ?: 0L) + 1L)
            .also(lectores::add)
    }

    override suspend fun listar(): List<Lector> {
        if (fallarAlListar) error("Fallo simulado al listar lectores")
        return lectores.toList()
    }
}
