# KMP Crypto/Fiat Balance & Conversion App


Aplicación móvil multiplataforma (Android & iOS) challenge técnico Raya.

## ✨ Features

* Visualización de saldos para ARS, USD, BTC y ETH.
* Historial de transacciones del usuario.
* Conversión de moneda/cripto entre los saldos disponibles.
* Consulta de tasas de cambio utilizando APIs externas (CoinGecko, Exchangerate).

## 📱 Flujo de la Aplicación

La aplicación consta de dos pantallas principales implementadas con Compose Multiplatform:

1.  **Pantalla de Saldos y Transacciones:** Muestra los saldos actuales y el historial de transacciones.
2.  **Pantalla de Conversión:** Permite seleccionar monedas, ingresar monto, ver tasa y confirmar la conversión.
3.  **Componentes Adicionales:** Diálogos/Sheets para feedback al usuario (confirmaciones, errores).

## 📂 Project Structure

El proyecto sigue la estructura estándar de Compose Multiplatform con Clean Architecture en `commonMain` (`domain`, `data`, `presentation`).

* `composeApp`: Módulo principal.
    * `commonMain`: Lógica compartida y ViewModels.
    * `androidMain`: Implementación Android.
    * `iosMain`: Implementación iOS.
    * `commonTest`: Tests compartidos.

## 🛠️ Tech Stack & Architecture

Kotlin Multiplatform (~99% código compartido), Clean Architecture.

* **UI & State:** Compose Multiplatform, Navigation Compose, MVVM (`StateFlow`/`SharedFlow`).
* **Networking:** Ktor Client, Kotlinx Serialization.
* **External APIs:** CoinGecko (Cripto<->Fiat), Exchangerate (Fiat<->Fiat).
* **Database:** SQLDelight.
* **DI:** Koin.
* **Testing:** `kotlin.test`, `kotlinx.coroutines.test`.
* **Error Handling:** `Result` wrapper, Custom Domain Exceptions, Safe Calls.
* **Resources:** `commonMain/composeResources`.
* **Build & Secrets:** Gradle, Version Catalogs, Custom Gradle task para `Secrets.kt` desde `local.properties`.
* **Platform-Specific:** `expect`/`actual` (SQLDelight Driver, Ktor Engine).

## 🚀 Setup & Installation

1.  **Clonar el Repositorio:**
    ```bash
    git clone [URL_DE_TU_REPOSITORIO]
    cd [NOMBRE_DEL_DIRECTORIO]
    ```

2.  **Configurar API Keys:**
    * Obtén claves API (planes gratuitos disponibles) de:
        * **CoinGecko:** [https://www.coingecko.com/en/api](https://www.coingecko.com/en/api)
        * **Exchangerate:** [https://exchangerate-api.com/](https://exchangerate-api.com/)
    * Crea un archivo `local.properties` en la raíz del proyecto.
    * Añade claves al archivo:
        ```properties
        COIN_GECKO_KEY=YOUR_COINGECKO_KEY_HERE
        EXCHANGE_KEY=YOUR_EXCHANGERATE_KEY_HERE
        ```

3.  **Sincronizar el Proyecto:**
    * Abre el proyecto en Android Studio.
    * Sincroniza con Gradle (`File > Sync Project with Gradle Files`) para generar los secrets.

## ▶️ Running the App

**Prerrequisitos:**

* Android Studio (versión compatible con KMP, ej. Iguana, Koala o superior).
* Para Android: Un Emulador de Android configurado o un dispositivo físico conectado.
* Para iOS:
    * macOS.
    * Xcode instalado (para las herramientas de build y simuladores).
    * Un Simulador de iOS configurado a través de Xcode.

**Ejecución:**

1.  **Abrir el Proyecto:** Abre el proyecto clonado en Android Studio.
2.  **Seleccionar Configuración:** En la barra de configuraciones de ejecución/depuración:
    * **Para Android:** Selecciona `composeApp` (o `androidApp`).
    * **Para iOS:** Selecciona `iosApp`.
3.  **Seleccionar Dispositivo:**
    * **Para Android:** Elige un emulador disponible o un dispositivo conectado.
    * **Para iOS:** Elige un simulador de iOS disponible (previamente configurado en Xcode).
4.  **Ejecutar:** Haz clic en el botón 'Run' (▶️) o 'Debug' (🐞).

La aplicación debería compilarse e instalarse en el emulador/simulador/dispositivo seleccionado.

## 🤔 Rationale & Decisions

*(Las decisiones clave sobre tecnología y arquitectura se han integrado en las secciones anteriores. Se priorizó KMP con Compose para máxima reutilización de código, Clean Architecture para mantenibilidad y testabilidad, y se seleccionaron librerías robustas y estándar en el ecosistema KMP como Ktor, SQLDelight y Koin).*

---
