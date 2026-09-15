package pe.edu.upeu.bibliomobil.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module { }
val domainModule = module { }
val presentationModule = module { }

expect val platformModule: Module

fun initKoin(config: KoinApplication.() -> Unit = {}): KoinApplication =
    startKoin {
        config()
        modules(dataModule, domainModule, presentationModule, platformModule)
    }
