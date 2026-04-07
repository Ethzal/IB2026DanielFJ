# <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Activity/Sparkles.webp" alt="Sparkles" width="25" height="25" /> IB2026 DanielFJ - Energy Invoices App

Este proyecto es la implementación profesional de una aplicación Android nativa para la gestión de facturas. El enfoque principal ha sido la aplicación de **Clean Architecture**, principios **SOLID** y una interfaz moderna construida íntegramente con **Jetpack Compose**.

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Camera%20with%20Flash.png" alt="Camera with Flash" width="25" height="25" /> Showcase Visual

La interfaz sigue los lineamientos de diseño de la aplicación oficial, garantizando una experiencia de usuario (UX) coherente y profesional.

| <img src="https://github.com/user-attachments/assets/d5864f11-c723-48b0-8d61-99bfe93f043e" width="250" alt="Home"/> | <img src="https://github.com/user-attachments/assets/294e8931-81ac-4ee0-a143-592062501667" width="250" alt="Tabs"/> | <img src="https://github.com/user-attachments/assets/33d34258-02ae-42db-aae8-c201379a5f48" width="250" alt="Feedback"/> |
| :---: | :---: | :---: |
| <sub><b>Pantalla Principal / Home</b></sub> | <sub><b>Listado de Facturas (Tabs)</b></sub> | <sub><b>Feedback BottomSheet</b></sub> |
| <img src="https://github.com/user-attachments/assets/2079b741-c8ec-4cef-aafb-e9784345b33a" width="250" alt="Filtros"/> | <img src="https://github.com/user-attachments/assets/eeefa89a-ac12-4e9e-8ac8-50f6ec41cab1" width="250" alt="Filtradas"/> | <img src="https://github.com/user-attachments/assets/ff3d8209-709d-4c44-a015-fea62f5bccc3" width="250" alt="Empty"/> |
| <sub><b>Pantalla Filtros</b></sub> | <sub><b>Facturas Filtradas</b></sub> | <sub><b>Empty State</b></sub> |

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Rocket.png" alt="Rocket" width="25" height="25" /> Características Destacadas

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blueviolet?style=for-the-badge&logo=kotlin)
![Compose](https://img.shields.io/badge/Jetpack_Compose-UI-orange?style=for-the-badge&logo=jetpackcompose)
![Architecture](https://img.shields.io/badge/Clean_Architecture-Multimodule-blue?style=for-the-badge)
![Hilt](https://img.shields.io/badge/Hilt_DI-Implementado-orange?style=for-the-badge)
![Flow](https://img.shields.io/badge/Kotlin_Flow-Reactivo-yellow?style=for-the-badge)

*   **Arquitectura Multimódulo:** Separación física de capas para garantizar independencia y escalabilidad.
*   **Gestión Híbrida de Datos (Dual-Source):**
    *   **Modo Local:** Uso de `Retromock` para servir datos desde archivos JSON locales (`assets`) con simulación de latencia aleatoria (1-3s).
    *   **Modo Remoto:** Integración con `Retrofit` para consumo de APIs reales o simuladas mediante **Mockoon**. Se incluye el archivo de environment en ``app/src/main/assets/invoiceMockoonEnvironment.json``, para reproducir fácilmente las peticiones de red utilizadas en la app.
*   **Caché Offline:** Implementación de `Room Database` para persistencia de datos y consulta sin conexión.
*   **UI Reactiva y Fluida:** Uso de `StateFlow` y estados de Compose (`mutableStateOf`) para reflejar cambios de estado de forma instantánea y segura ante cambios de configuración.
*   **Skeleton Loading:** Implementación de Shimmer animado nativo en Compose.
*   **Sistema de Filtrado Avanzado:** Implementación de una pantalla dedicada para filtrar facturas por Rango de fechas (DatePicker), Rango de importe (RangeSlider dinámico calculado según los datos) y Estado (Selección múltiple).
*   **Sistema de Feedback Inteligente:** Un BottomSheet nativo en Compose para recoger la valoración del usuario. Implementa lógica de negocio (UseCases) para mostrarse de forma no intrusiva basándose en un sistema de contadores (seguimiento de veces que el usuario intenta salir de la app) almacenado en DataStore.
*   **Navegación Paginada (Tabs):** Uso de HorizontalPager de Compose para separar visualmente el histórico de facturas de "Luz" y "Gas" con transiciones fluidas.
*   **Toggle Dinámico de Entorno:** Switch en la pantalla principal (HomeScreen) que permite cambiar en tiempo real entre el entorno Local (Mocks) y Remoto (API/Mockoon) persistiendo la decisión en las preferencias locales.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Travel%20and%20Places/Classical%20Building.webp" alt="Classical Building" width="25" height="25" /> Arquitectura y Principios de Diseño

El proyecto utiliza una estructura de **Clean Architecture** para desacoplar la lógica de negocio de los detalles de implementación:

1.  **Capa de Presentación (Presentation):** Patrón **MVVM**. Los `ViewModels` exponen el estado mediante `Flow`. La UI está construida con funciones **Composable** organizadas en un sistema de diseño atómico.
2.  **Capa de Dominio (Domain):** Contiene la lógica de negocio pura. Es un módulo **100% Kotlin (JVM)**, libre de dependencias de Android, facilitando el testeo unitario.
3.  **Capa de Datos (Data):** Implementa el patrón *Repository*. Orquesta el flujo de datos entre la red (`Retrofit/Retromock`) y la base de datos local (`Room`).

### Estructura de Módulos
- **`:app`** — Punto de entrada, configuración de `Hilt` y navegación global.
- **`:domain`** — Casos de uso (`UseCases`), modelos de dominio e interfaces de repositorio.
- **`:data`** — Implementación de repositorios, APIs, DAOs y DTOs (Mappers).
- **`:presentation`** — Pantallas (`Screens`), componentes reutilizables y temas (Color, Type, Shape).
- **`:core`** — Módulos de utilidades comunes.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Objects/Toolbox.webp" alt="Toolbox" width="25" height="25" /> Stack Tecnológico

| Componente                    | Tecnología / Librería           |
|:------------------------------|:--------------------------------|
| **Lenguaje**                  | Kotlin                          |
| **Inyección de Dependencias** | Dagger Hilt                     |
| **Networking**                | Retrofit 2, OkHttp 4, Retromock |
| **Persistencia**              | Room Database & DataStore       |
| **Asincronía**                | Coroutines & Kotlin Flow        |
| **Interfaz de Usuario**       | Jetpack Compose, Material 3     |
| **Carga Visual**              | Shimmer Animation (Compose)     |
| **Navegación**                | Navigation & Pager Compose      |
| **Testing**                   | JUnit 4, Mockk                  |
