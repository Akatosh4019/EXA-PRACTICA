package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun LibroScreen(viewModel: LibroViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Libros",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
        item {
            FormularioDeLibro(
                formulario = estado.formulario,
                registrando = estado.registrando,
                onTituloChange = viewModel::onTituloChange,
                onAutorChange = viewModel::onAutorChange,
                onAnioChange = viewModel::onAnioChange,
                onEjemplaresChange = viewModel::onEjemplaresChange,
                onRegistrar = viewModel::registrar,
            )
        }
        estado.mensajeExito?.let { mensaje ->
            item { Text(mensaje, color = MaterialTheme.colorScheme.primary) }
        }
        when (val fase = estado.fase) {
            LibroFase.Cargando -> item {
                EstadoCargando()
            }
            LibroFase.SinLibros -> item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("0 libros", style = MaterialTheme.typography.titleLarge)
                    Text("Aún no hay libros registrados")
                }
            }
            is LibroFase.ConLibros -> {
                item {
                    Text(conteoLibros(fase.libros.size), style = MaterialTheme.typography.titleLarge)
                }
                items(fase.libros, key = LibroUi::id) { libro ->
                    TarjetaLibro(libro)
                }
            }
            is LibroFase.Error -> item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(fase.mensaje, color = MaterialTheme.colorScheme.error)
                    Button(onClick = viewModel::cargarLibros) { Text("Reintentar") }
                }
            }
        }
        item { Text("", modifier = Modifier.padding(bottom = 12.dp)) }
    }
}

@Composable
private fun FormularioDeLibro(
    formulario: FormularioLibro,
    registrando: Boolean,
    onTituloChange: (String) -> Unit,
    onAutorChange: (String) -> Unit,
    onAnioChange: (String) -> Unit,
    onEjemplaresChange: (String) -> Unit,
    onRegistrar: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CampoLibro("Título", formulario.titulo, formulario.tituloError, onTituloChange)
            CampoLibro("Autor", formulario.autor, formulario.autorError, onAutorChange)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CampoLibro(
                    etiqueta = "Año",
                    valor = formulario.anio,
                    error = formulario.anioError,
                    onValueChange = onAnioChange,
                    numerico = true,
                    modifier = Modifier.weight(1f),
                )
                CampoLibro(
                    etiqueta = "Ejemplares",
                    valor = formulario.ejemplares,
                    error = formulario.ejemplaresError,
                    onValueChange = onEjemplaresChange,
                    numerico = true,
                    modifier = Modifier.weight(1f),
                )
            }
            Button(
                onClick = onRegistrar,
                enabled = !registrando,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (registrando) "Registrando…" else "Registrar")
            }
        }
    }
}

@Composable
private fun CampoLibro(
    etiqueta: String,
    valor: String,
    error: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    numerico: Boolean = false,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = error?.let { mensaje -> ({ Text(mensaje) }) },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (numerico) KeyboardType.Number else KeyboardType.Text,
        ),
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun EstadoCargando() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CircularProgressIndicator()
        Text("Cargando catálogo…")
    }
}

@Composable
private fun TarjetaLibro(libro: LibroUi) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
                if (libro.requiereReposicion) {
                    Text("Pocos ejemplares", color = MaterialTheme.colorScheme.error)
                }
            }
            Text(libro.autor)
            Text(libro.lineaSecundaria)
        }
    }
}

private fun conteoLibros(cantidad: Int): String =
    if (cantidad == 1) "1 libro" else "$cantidad libros"
