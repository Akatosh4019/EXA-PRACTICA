package pe.edu.upeu.bibliomobil.di

import org.koin.core.context.stopKoin
import org.koin.core.context.startKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class AppModuleTest {
    @AfterTest
    fun limpiarKoin() {
        stopKoin()
    }

    @Test
    fun resuelveLibroRepositoryComoImplementacionEnMemoria() {
        val koin = iniciarKoin()
        assertIs<LibroRepositorioEnMemoria>(koin.get<LibroRepository>())
    }

    @Test
    fun libroRepositoryEsUnico() {
        val koin = iniciarKoin()
        assertSame(koin.get<LibroRepository>(), koin.get<LibroRepository>())
    }

    @Test
    fun resuelveLosCuatroCasosDeUso() {
        val koin = iniciarKoin()
        assertIs<RegistrarLibroUseCase>(koin.get<RegistrarLibroUseCase>())
        assertIs<ListarLibrosUseCase>(koin.get<ListarLibrosUseCase>())
        assertIs<RegistrarLectorUseCase>(koin.get<RegistrarLectorUseCase>())
        assertIs<ListarLectoresUseCase>(koin.get<ListarLectoresUseCase>())
    }

    private fun iniciarKoin() = startKoin {
        modules(dataModule, domainModule)
    }.koin
}
