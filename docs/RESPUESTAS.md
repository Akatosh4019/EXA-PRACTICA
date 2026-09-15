# Respuestas del examen práctico

**Estudiante:** Roberto Samuel Valencia Saavedra  
**Docente:** Benjamín David Reyna Barreto  
**Curso:** Desarrollo de Aplicaciones Móviles

## 1. Cambio hacia un backend REST

Cuando la biblioteca incorpore un backend REST, cambiarán principalmente la capa `data` y la configuración de `di`: se añadirá una implementación remota de `LibroRepository` y `LectorRepository`, junto con el cliente y los modelos de transporte necesarios, y Koin pasará a registrar esas nuevas implementaciones. Los modelos, contratos y casos de uso de `domain`, así como los ViewModels y pantallas de `presentation`, quedarán intactos porque dependen de las interfaces del dominio y no conocen si los datos vienen de memoria, de una base de datos o de Internet; esa es la regla de dependencia aplicada en el proyecto.

## 2. Campos numéricos recibidos como String

Si `RegistrarLibroUseCase` recibiera `anio` y `ejemplares` como `Int`, se perdería la posibilidad de distinguir entre un campo vacío, un texto que no es un número y un número fuera del rango permitido. La conversión tendría que realizarla la pantalla o el ViewModel, trasladando reglas de validación fuera del dominio y acoplando la presentación a decisiones que corresponden al caso de uso; al recibir `String`, el dominio puede validar cada situación y devolver el mensaje exacto para el campo correspondiente.

## 3. Repositorio registrado como factory

Si `LibroRepository` estuviera registrado como `factory`, Koin crearía una instancia nueva cada vez que un caso de uso la solicitara. El usuario podría registrar un libro mediante una instancia y, al actualizar el catálogo con otra, encontrar la lista vacía porque cada repositorio en memoria conservaría su propio estado. Al registrarlo como `single`, los casos de uso comparten la misma instancia y los libros registrados permanecen disponibles durante toda la ejecución de la aplicación.

## Resultado de las pruebas

Comando ejecutado:

```powershell
.\gradlew.bat --no-configuration-cache :shared:clean :shared:testAndroidHostTest
```

Resultado consolidado de los archivos XML generados en `shared/build/test-results/testAndroidHostTest`:

```text
Tests ejecutados: 34
Fallos: 0
Errores: 0
Omitidos: 0

> Task :shared:testAndroidHostTest
BUILD SUCCESSFUL in 4s
35 actionable tasks: 22 executed, 11 from cache, 2 up-to-date
```

La ejecución completa puede verificarse en la captura [`09-pruebas-build-successful.png`](CAPTURAS/09-pruebas-build-successful.png).
