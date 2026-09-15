package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.navigation.Screen
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import pe.edu.upeu.bibliomobil.theme.BiblioMobilTheme

data class Destino(
    val screen: Screen,
    val titulo: String,
    val icono: ImageVector,
)

val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio", Icons.Default.Home),
    Destino(Screen.Libros, "Libros", Icons.AutoMirrored.Filled.MenuBook),
    Destino(Screen.Lectores, "Lectores", Icons.Default.People),
    Destino(Screen.Prestamos, "Préstamos", Icons.Default.Bookmark),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    KoinContext {
        var darkTheme by rememberSaveable { mutableStateOf(false) }
        BiblioMobilTheme(darkTheme = darkTheme) {
            var pantallaActual by rememberSaveable(stateSaver = Screen.Saver) {
                mutableStateOf<Screen>(Screen.Inicio)
            }
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        MenuPrincipal(
                            pantallaActual = pantallaActual,
                            darkTheme = darkTheme,
                            onDarkThemeChange = { darkTheme = it },
                            onSeleccionar = { screen ->
                                pantallaActual = screen
                                scope.launch { drawerState.close() }
                            },
                        )
                    }
                },
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(tituloDe(pantallaActual)) },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                                }
                            },
                        )
                    },
                ) { paddingValues ->
                    ContenidoActual(
                        pantalla = pantallaActual,
                        onNavegar = { pantallaActual = it },
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuPrincipal(
    pantallaActual: Screen,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onSeleccionar: (Screen) -> Unit,
) {
    Column(modifier = Modifier.fillMaxHeight().padding(horizontal = 12.dp)) {
        Text("BiblioMobil", modifier = Modifier.padding(16.dp))
        DESTINOS.forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                selected = pantallaActual == destino.screen,
                onClick = { onSeleccionar(destino.screen) },
                icon = { Icon(destino.icono, contentDescription = null) },
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Modo oscuro", modifier = Modifier.weight(1f))
            Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange)
        }
    }
}

@Composable
private fun ContenidoActual(
    pantalla: Screen,
    onNavegar: (Screen) -> Unit,
    modifier: Modifier,
) {
    when (pantalla) {
        Screen.Inicio -> InicioScreen(onNavegar = onNavegar, modifier = modifier)
        Screen.Libros -> LibroScreen(
            viewModel = koinViewModel<LibroViewModel>(),
            modifier = modifier,
        )
        Screen.Lectores -> LectorScreen(
            viewModel = koinViewModel<LectorViewModel>(),
            modifier = modifier,
        )
        Screen.Prestamos -> EstadoVacio(
            icono = Icons.Default.Bookmark,
            titulo = "Préstamos en construcción",
            descripcion = "Pronto podrás registrar y revisar los préstamos de la biblioteca.",
            modifier = modifier,
        )
    }
}

private fun tituloDe(screen: Screen): String =
    DESTINOS.first { it.screen == screen }.titulo
