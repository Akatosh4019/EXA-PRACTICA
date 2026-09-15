package pe.edu.upeu.bibliomobil.navigation

import androidx.compose.runtime.saveable.Saver

sealed class Screen(val clave: String) {
    data object Inicio : Screen("inicio")
    data object Libros : Screen("libros")
    data object Lectores : Screen("lectores")
    data object Prestamos : Screen("prestamos")

    companion object {
        val Saver = Saver<Screen, String>(
            save = { screen -> screen.clave },
            restore = { clave ->
                when (clave) {
                    Libros.clave -> Libros
                    Lectores.clave -> Lectores
                    Prestamos.clave -> Prestamos
                    else -> Inicio
                }
            },
        )
    }
}
