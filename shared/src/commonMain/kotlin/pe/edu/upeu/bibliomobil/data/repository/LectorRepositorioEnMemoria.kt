package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class LectorRepositorioEnMemoria : LectorRepository {
    private val acceso = Mutex()
    private val lectoresRegistrados = mutableListOf<Lector>()
    private var proximoId = 1L

    override suspend fun registrar(lector: Lector): Lector {
        simularLatencia()
        return acceso.withLock {
            lector.copy(id = proximoId++).also(lectoresRegistrados::add)
        }
    }

    override suspend fun listar(): List<Lector> {
        simularLatencia()
        return acceso.withLock { lectoresRegistrados.toList() }
    }

    private suspend fun simularLatencia() {
        delay(LATENCIA_MS)
    }

    private companion object {
        const val LATENCIA_MS = 400L
    }
}
