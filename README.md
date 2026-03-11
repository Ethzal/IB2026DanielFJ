# <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Activity/Sparkles.webp" alt="Sparkles" width="25" height="25" /> IB2026 DanielFJ - Energy Invoices App

Este proyecto es la implementación profesional de una aplicación Android nativa para la gestión de facturas. El enfoque principal ha sido la aplicación de **Clean Architecture**, principios **SOLID** y reactividad con **Kotlin Flow**.

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Camera%20with%20Flash.png" alt="Camera with Flash" width="25" height="25" /> Showcase Visual

La interfaz sigue los lineamientos de diseño de la aplicación oficial, garantizando una experiencia de usuario (UX) coherente y profesional.

<p align="center">
  <img src="https://github.com/user-attachments/assets/6cbc5dac-ca04-480d-8ec5-74bf60b975ab" width="350" alt="App Screenshot">
</p>

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Rocket.png" alt="Rocket" width="25" height="25" /> Características Destacadas

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blueviolet?style=for-the-badge&logo=kotlin)
![Architecture](https://img.shields.io/badge/Clean_Architecture-Multimodule-blue?style=for-the-badge)
![Hilt](https://img.shields.io/badge/Hilt_DI-Implementado-orange?style=for-the-badge)
![Flow](https://img.shields.io/badge/Kotlin_Flow-Reactivo-yellow?style=for-the-badge)

*   **Arquitectura Multimódulo:** Separación física de capas para garantizar independencia y escalabilidad.
*   **Gestión Híbrida de Datos (Dual-Source):**
    *   **Modo Local:** Uso de `Retromock` para servir datos desde archivos JSON locales (`assets`) con simulación de latencia aleatoria (1-3s).
    *   **Modo Remoto:** Integración con `Retrofit` para consumo de APIs reales o simuladas mediante **Mockoon**.
*   **Caché Offline:** Implementación de `Room Database` para persistencia de datos y consulta sin conexión.
*   **UI Reactiva y Fluida:** Uso de `ViewBinding` y `StateFlow` para reflejar cambios de estado de forma instantánea.
*   **Skeleton Loading:** Implementación de `Shimmer` para una carga visual elegante.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Travel%20and%20Places/Classical%20Building.webp" alt="Classical Building" width="25" height="25" /> Arquitectura y Principios de Diseño

El proyecto utiliza una estructura de **Clean Architecture** para desacoplar la lógica de negocio de los detalles de implementación:

1.  **Capa de Presentación (Presentation):** Patrón **MVVM**. Los `ViewModels` gestionan el estado mediante `Flow`, comunicándose con la vista (`Fragments`) a través de estados sellados (`UiState`).
2.  **Capa de Dominio (Domain):** Contiene la lógica de negocio pura. Es un módulo **100% Kotlin (JVM)**, libre de dependencias de Android, facilitando el testeo unitario.
3.  **Capa de Datos (Data):** Implementa el patrón *Repository*. Orquesta el flujo de datos entre la red (`Retrofit/Retromock`) y la base de datos local (`Room`).

### Estructura de Módulos
- **`:app`** — Punto de entrada, configuración de `Hilt` y navegación global.
- **`:domain`** — Casos de uso (`UseCases`), modelos de dominio e interfaces de repositorio.
- **`:data`** — Implementación de repositorios, APIs, DAOs y DTOs (Mappers).
- **`:presentation`** — Fragmentos, Adapters de RecyclerView y lógica de UI.
- **`:core`** — Módulos de utilidades comunes.

---

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Objects/Toolbox.webp" alt="Toolbox" width="25" height="25" /> Stack Tecnológico

| Componente | Tecnología / Librería                   |
| :--- |:----------------------------------------|
| **Lenguaje** | Kotlin                                  |
| **Inyección de Dependencias** | Dagger Hilt                             |
| **Networking** | Retrofit 2, OkHttp 4, Retromock         |
| **Persistencia** | Room Database                           |
| **Asincronía** | Coroutines & Kotlin Flow                |
| **Interfaz de Usuario** | XML, Fragments, ViewBinding, Material 3 |
| **Carga Visual** | Shimmer                                 |
| **Navegación** | Jetpack Navigation Component            |
