package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException

suspend inline fun <T> resultadoDe(crossinline operacion: suspend () -> T): Result<T> =
    try {
        Result.success(operacion())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        Result.failure(error)
    }
