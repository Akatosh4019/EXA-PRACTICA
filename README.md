# BiblioMobil

Proyecto Kotlin Multiplatform para la gestión de una biblioteca, desarrollado como parte del examen práctico del curso Desarrollo de Aplicaciones Móviles.

## Configuración inicial

- Paquete raíz: `pe.edu.upeu.bibliomobil`
- Aplicación Android: `pe.edu.upeu.bibliomobil`
- Plataformas: Android e iOS
- Interfaz: Compose Multiplatform y Material 3
- Inyección de dependencias: Koin
- Concurrencia: Kotlin Coroutines
- Arquitectura prevista: Clean Architecture y MVVM

## Ejecución en Android

Desde la raíz del proyecto:

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

El archivo `local.properties` no se incluye en Git porque contiene la ruta local del SDK de Android.
