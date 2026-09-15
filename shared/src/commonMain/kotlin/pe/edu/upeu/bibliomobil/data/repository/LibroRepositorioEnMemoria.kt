package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class LibroRepositorioEnMemoria : LibroRepository {
    private val acceso = Mutex()
    private val catalogo = mutableListOf<Libro>()
    private var proximoId = 1L

    override suspend fun registrar(libro: Libro): Libro {
        simularLatencia()
        return acceso.withLock {
            libro.copy(id = proximoId++).also(catalogo::add)
        }
    }

    override suspend fun listar(): List<Libro> {
        simularLatencia()
        return acceso.withLock { catalogo.toList() }
    }

    private suspend fun simularLatencia() {
        delay(LATENCIA_MS)
    }

    private companion object {
        const val LATENCIA_MS = 400L
    }
}
