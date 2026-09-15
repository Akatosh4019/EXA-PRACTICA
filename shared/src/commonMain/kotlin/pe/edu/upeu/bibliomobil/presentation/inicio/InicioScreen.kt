package pe.edu.upeu.bibliomobil.presentation.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.navigation.Screen

private data class AccesoRapido(
    val texto: String,
    val destino: Screen,
    val icono: ImageVector,
)

private val accesosRapidos = listOf(
    AccesoRapido("Registrar libros", Screen.Libros, Icons.AutoMirrored.Filled.MenuBook),
    AccesoRapido("Registrar lectores", Screen.Lectores, Icons.Default.People),
    AccesoRapido("Revisar préstamos", Screen.Prestamos, Icons.Default.Bookmark),
)

@Composable
fun InicioScreen(
    onNavegar: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.LocalLibrary,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text("BiblioMobil", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Tu biblioteca, siempre contigo",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 28.dp),
        )
        Text(
            text = "Qué puedes hacer",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        )
        accesosRapidos.forEach { acceso ->
            Button(
                onClick = { onNavegar(acceso.destino) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            ) {
                Icon(acceso.icono, contentDescription = null)
                Text(acceso.texto, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
