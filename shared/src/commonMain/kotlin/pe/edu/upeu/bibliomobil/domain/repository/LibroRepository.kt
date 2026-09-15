package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/** Define las operaciones del catálogo sin depender de una fuente de datos concreta. */
interface LibroRepository {
    suspend fun registrar(libro: Libro): Libro
    suspend fun listar(): List<Libro>
}
