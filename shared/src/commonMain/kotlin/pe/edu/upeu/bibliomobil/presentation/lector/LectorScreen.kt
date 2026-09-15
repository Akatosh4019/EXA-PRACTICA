package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@Composable
fun LectorScreen(viewModel: LectorViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Lectores",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
        item {
            FormularioDeLector(
                formulario = estado.formulario,
                registrando = estado.registrando,
                onNombreChange = viewModel::onNombreChange,
                onCorreoChange = viewModel::onCorreoChange,
                onTelefonoChange = viewModel::onTelefonoChange,
                onRegistrar = viewModel::registrar,
            )
        }
        estado.mensajeExito?.let { mensaje ->
            item { MensajeExito(mensaje) }
        }
        when (val fase = estado.fase) {
            LectorFase.Cargando -> item { EstadoCargandoLectores() }
            LectorFase.SinLectores -> item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("0 lectores", style = MaterialTheme.typography.titleLarge)
                    EstadoVacio(
                        icono = Icons.Default.People,
                        titulo = "Sin lectores",
                        descripcion = "Aún no hay lectores registrados",
                    )
                }
            }
            is LectorFase.ConLectores -> {
                item {
                    Text(conteoLectores(fase.lectores.size), style = MaterialTheme.typography.titleLarge)
                }
                items(fase.lectores, key = LectorUi::id) { lector ->
                    TarjetaLector(lector)
                }
            }
            is LectorFase.Error -> item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    EstadoVacio(
                        icono = Icons.Default.People,
                        titulo = "No se pudo cargar",
                        descripcion = fase.mensaje,
                        color = MaterialTheme.colorScheme.error,
                    )
                    Button(onClick = viewModel::cargarLectores) { Text("Reintentar") }
                }
            }
        }
        item { Text("", modifier = Modifier.padding(bottom = 12.dp)) }
    }
}

@Composable
private fun FormularioDeLector(
    formulario: FormularioLector,
    registrando: Boolean,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onRegistrar: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CampoLector(
                etiqueta = "Nombre",
                valor = formulario.nombre,
                error = formulario.nombreError,
                onValueChange = onNombreChange,
            )
            CampoLector(
                etiqueta = "Correo",
                valor = formulario.correo,
                error = formulario.correoError,
                onValueChange = onCorreoChange,
                keyboardType = KeyboardType.Email,
            )
            CampoLector(
                etiqueta = "Teléfono (opcional)",
                valor = formulario.telefono,
                error = formulario.telefonoError,
                onValueChange = onTelefonoChange,
                keyboardType = KeyboardType.Phone,
            )
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
private fun CampoLector(
    etiqueta: String,
    valor: String,
    error: String?,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    ValidatedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = etiqueta,
        error = error,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun EstadoCargandoLectores() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CircularProgressIndicator()
        Text("Cargando lectores…")
    }
}

@Composable
private fun TarjetaLector(lector: LectorUi) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(lector.nombre, style = MaterialTheme.typography.titleMedium)
            Text(lector.correo)
            Text(lector.telefono)
        }
    }
}

private fun conteoLectores(cantidad: Int): String =
    if (cantidad == 1) "1 lector" else "$cantidad lectores"
