package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/** Define las operaciones de lectores sin depender de una fuente de datos concreta. */
interface LectorRepository {
    suspend fun registrar(lector: Lector): Lector
    suspend fun listar(): List<Lector>
}
