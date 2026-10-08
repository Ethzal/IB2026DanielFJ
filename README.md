# <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Activity/Sparkles.webp" alt="Sparkles" width="25" height="25" /> IB2026 DanielFJ — Aplicación Android de gestión de facturas

Aplicación Android nativa para la consulta y gestión de facturas energéticas, desarrollada durante mi etapa de prácticas en **Viewnext**.

El proyecto está construido con **Kotlin** y **Jetpack Compose**, siguiendo **Clean Architecture** y **MVVM**, con una arquitectura multimódulo orientada a separar la lógica de negocio, los datos y la interfaz de usuario.

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Camera%20with%20Flash.png" alt="Camera with Flash" width="25" height="25" /> Showcase Visual

La interfaz está basada en los lineamientos de diseño de la aplicación de referencia, adaptándolos a una implementación moderna con **Jetpack Compose** y **Material 3**.

| <img src="https://github.com/user-attachments/assets/9813b936-7d34-42c6-93b0-37138311a79b" width="250" alt="Home"/> | <img src="https://github.com/user-attachments/assets/3e5f6891-1ffc-4e81-b0b7-c347e5414e31" width="250" alt="Tabs"/> | <img src="https://github.com/user-attachments/assets/87d0cc80-255f-4ca6-8328-24d42d8a11ac" width="250" alt="Feedback"/> |
| :---: | :---: | :---: |
| <sub><b>Pantalla Principal / Home</b></sub> | <sub><b>Listado de Facturas</b></sub> | <sub><b>Feedback BottomSheet</b></sub> |
| <img src="https://github.com/user-attachments/assets/c8713b34-e864-459e-b218-52bc537b756d" width="250" alt="Filtros"/> | <img src="https://github.com/user-attachments/assets/be2aa6c6-9881-4a81-8310-76818d00e7fc" width="250" alt="Filtradas"/> | <img src="https://github.com/user-attachments/assets/45942d8f-9614-44e8-ac53-ec321fa423a6" width="250" alt="Empty"/> |
| <sub><b>Filtros avanzados</b></sub> | <sub><b>Facturas filtradas</b></sub> | <sub><b>Empty State</b></sub> |

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Rocket.png" alt="Rocket" width="25" height="25" /> Características principales

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blueviolet?style=for-the-badge&logo=kotlin)
![Compose](https://img.shields.io/badge/Jetpack_Compose-UI-orange?style=for-the-badge&logo=jetpackcompose)
![Architecture](https://img.shields.io/badge/Clean_Architecture-Multimodule-blue?style=for-the-badge)
![Hilt](https://img.shields.io/badge/Hilt_DI-Implementado-orange?style=for-the-badge&logo=android)
![Flow](https://img.shields.io/badge/Kotlin_Flow-Reactivo-yellow?style=for-the-badge&logo=kotlin)

- **Arquitectura multimódulo:** separación entre `app`, `domain`, `data`, `presentation` y `core`.
- **Clean Architecture + MVVM:** separación de responsabilidades y desacoplamiento de la lógica de negocio.
- **Gestión híbrida de datos:** posibilidad de trabajar con datos locales o mediante una API remota.
- **Caché offline:** persistencia de facturas mediante **Room Database**.
- **UI reactiva:** uso de **StateFlow**, Coroutines y estados de Compose para gestionar los cambios de estado.
- **Skeleton Loading:** indicadores de carga mediante Shimmer animado.
- **Filtrado avanzado:** filtros por rango de fechas, importe y estado mediante `DatePicker`, `RangeSlider` y selección múltiple.
- **Navegación mediante tabs:** separación entre facturas de **Luz** y **Gas** utilizando `HorizontalPager`.
- **Cambio de entorno:** selector para alternar entre datos locales y entorno remoto, manteniendo la configuración mediante **DataStore**.

### <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Objects/Toolbox.webp" alt="Toolbox" width="25" height="25" /> Datos y networking

El proyecto permite trabajar con dos fuentes de datos:

**Modo local**
- **Retromock** para simular las respuestas de la API.
- Datos almacenados en archivos JSON dentro de `assets`.
- Simulación de latencia de red para reproducir escenarios de carga.

**Modo remoto**
- Consumo de APIs mediante **Retrofit**.
- Entorno de pruebas reproducible mediante **Mockoon**.
- Archivo de configuración incluido en `app/src/main/res/raw/mockoon_iberdrola.json`.

La aplicación puede cambiar entre ambos entornos desde la propia interfaz.

### <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Objects/Mobile%20Phone%20With%20Arrow.webp" alt="Mobile Phone With Arrow" width="25" height="25" /> Flujos de usuario

Además de la consulta y filtrado de facturas, la aplicación implementa varios flujos de usuario:

- Consulta de facturas de luz y gas.
- Filtrado combinado por fecha, importe y estado.
- Persistencia y consulta de datos sin conexión.
- Sistema contextual de feedback mediante `BottomSheet`.
- Flujo de factura electrónica.
- Validación de dirección de email.
- Verificación mediante código **OTP**.
- Gestión de estados de carga, éxito y error.

El flujo de factura electrónica se estructura en diferentes pasos hasta completar la verificación:

`CONTRACT_LIST → MODIFY_INFO → EMAIL_INPUT → OTP → SUCCESS`

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Classical%20Building.png" alt="Classical Building" width="25" height="25" /> Arquitectura y principios de diseño

El proyecto utiliza **Clean Architecture** para separar la lógica de negocio de los detalles de implementación.

### Capas principales

**1. Presentation**

Implementa **MVVM**. Los `ViewModels` gestionan y exponen el estado mediante `Flow`, mientras que la interfaz está construida con funciones **Composable** y componentes reutilizables.

**2. Domain**

Contiene la lógica de negocio, modelos, interfaces de repositorio y `UseCases`. El módulo está desarrollado en Kotlin/JVM y no depende directamente de Android, facilitando su testeo.

**3. Data**

Implementa los repositorios y coordina las diferentes fuentes de datos: API mediante **Retrofit/Retromock** y almacenamiento local mediante **Room**.

### <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Objects/Toolbox.webp" alt="Toolbox" width="25" height="25" /> Estructura de módulos

```text
:app
 └── Punto de entrada, configuración de Hilt y navegación global

:domain
 └── Modelos, UseCases e interfaces de repositorio

:data
 └── Repositorios, APIs, DAOs, DTOs y Mappers

:presentation
 └── Screens, ViewModels, componentes reutilizables y temas

:core
 └── Utilidades y componentes compartidos
```

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Toolbox.png" alt="Toolbox" width="25" height="25" /> Stack tecnológico

| Componente | Tecnología / Librería |
|:---|:---|
| **Lenguaje** | Kotlin |
| **Arquitectura** | Clean Architecture + MVVM |
| **Inyección de dependencias** | Dagger Hilt |
| **Networking** | Retrofit 2 · OkHttp 4 · Retromock |
| **Persistencia** | Room Database · DataStore |
| **Asincronía** | Coroutines · Kotlin Flow |
| **Interfaz de usuario** | Jetpack Compose · Material 3 |
| **Navegación** | Navigation Compose · HorizontalPager |
| **Testing** | JUnit 4 · MockK |
| **Backend simulado** | Mockoon |
| **Servicios** | Firebase Analytics · Crashlytics · Remote Config |

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Symbols/Check%20Mark%20Button.png" alt="Check Mark Button" width="25" height="25" /> Testing

El proyecto incluye tests unitarios centrados principalmente en la lógica de negocio y los casos de uso.

Se utilizan:

- **JUnit 4**
- **MockK**
- **Coroutines Test**

Los tests permiten validar diferentes escenarios de los `UseCases` sin depender directamente de la interfaz de usuario o de los servicios externos.

---
