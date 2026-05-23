# Notely - Note Taking Android App

Notely is a simple note-taking application built with **Kotlin** and **Jetpack Compose** following the **MVVM** architecture with **Repository Pattern** for clean data management.

## 📋 Project Architecture

```
app/src/main/java/com/app/notely/
├── core/
│   ├── di/                # Hilt Dependency Injection modules
│   ├── navigation/        # Navigation routes (sealed class) & graph
│   ├── util/              # Utility functions (date formatting, constants)
│   └── extension/         # Compose Modifier extensions (noRippleClickable)
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAO interface with Flow-based CRUD
│   │   ├── entity/        # Room entity classes (NoteEntity)
│   │   └── mapper/        # Entity ↔ Domain model conversion
│   └── repository/        # Repository implementation (NoteRepositoryImpl)
├── domain/
│   ├── model/             # Domain model classes (Note)
│   └── repository/        # Repository interface contract
├── ui/
│   ├── feature/
│   │   └── list_note/     # List screen MVVM implementation
│   │       ├── component/ # Reusable card components (NoteItem)
│   │       ├── ListNoteViewModel.kt
│   │       ├── ListNoteUiState.kt
│   │       ├── ListNoteUiEvent.kt
│   │       └── ListNoteScreen.kt
│   ├── component/         # Shared UI components (EmptyView, LoadingView)
│   └── theme/             # Material3 theme (Color.kt, Theme.kt, Type.kt)
├── MainActivity.kt        # @AndroidEntryPoint Activity with NavHost
└── NotelyApplication.kt   # @HiltAndroidApp custom Application class
```

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Kotlin** | 2.3.21 | Programming language |
| **AGP** | 9.0.1 | Android Gradle Plugin |
| **Gradle** | 9.1.0 | Build system |
| **Jetpack Compose** | Latest | Modern UI framework |
| **Material3** | 2024.09.00 | Design system |
| **Room** | 2.6.1 | Local database (SQLite) |
| **Hilt** | 2.51 | Dependency injection |
| **KSP** | 2.3.8 | Kotlin Symbol Processing (annotation processing) |
| **Navigation Compose** | 2.7.7 | Composable-based navigation |
| **Coroutines** | Latest | Async programming |
| **Flow/StateFlow** | Latest | Reactive streams |
| **DataStore** | 1.0.0 | Preferences storage |
| **MockK** | 1.13.8 | Mocking library for tests |
| **Turbine** | 1.0.0 | Flow testing utility |

## 🚀 Getting Started

### Prerequisites
- Android Studio Jellyfish or newer
- JDK 11 or higher
- Gradle 9.1.0 (included via wrapper)

### Building the Project

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Notely
   ```

2. **Build the app**
   ```bash
   ./gradlew :app:assembleDebug
   ```

3. **Run on device or emulator**
   ```bash
   ./gradlew :app:installDebug
   ```

## 📁 Layer Architecture

### Core Layer (`core/`)
Shared infrastructure: database setup, DI modules, navigation routes, utilities, and extensions.

### Data Layer (`data/`)  
Data access with Room entities, DAOs, mappers, and repository implementation.

### Domain Layer (`domain/`)
Business logic and model contracts.

**Example - Domain Model & Repository:**
```kotlin
// domain/model/Note.kt
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val color: Int = 0xFFFFFF
)

// domain/repository/NoteRepository.kt
interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(id: String): Flow<Note?>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteNoteById(id: String)
}
```

### UI Layer (`ui/`)
Presentation with MVVM pattern using Jetpack Compose.

**Key files:** ListNoteViewModel, ListNoteUiState, ListNoteUiEvent, ListNoteScreen, and reusable components (NoteItem, EmptyView, LoadingView).

## 🧪 Testing Strategy

The project includes test scaffolding with mocking and Flow testing support:

### Unit Tests (JUnit 4 + MockK)
- **ListNoteViewModelTest.kt**: ViewModel unit tests with:
  - Repository mocking using MockK
  - StateFlow collection testing with Turbine
  - Event handling verification
  
- **NoteRepositoryImplTest.kt**: Repository tests with:
  - DAO mocking
  - Flow emission verification

### Test Framework Details
- **MockK 1.13.8**: Mocking framework for Kotlin
- **Turbine 1.0.0**: Flow testing utility for collecting and asserting emissions
- **Coroutines Test**: `kotlinx-coroutines-test 1.7.3` for dispatcher management

### Running Tests
```bash
# Run unit tests
./gradlew :app:testDebugUnitTest

# Run instrumented tests
./gradlew :app:connectedAndroidTest
```

## 📦 Build Configuration

### gradle/libs.versions.toml
Version catalog managing all dependencies:
```toml
[versions]
agp = "9.0.1"
kotlin = "2.3.21"
ksp = "2.3.8"
room = "2.6.1"
hilt = "2.51"
composeBom = "2024.09.00"

[libraries]
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
turbine = { group = "app.cash.turbine", name = "turbine", version.ref = "turbine" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

### app/build.gradle.kts
- **Plugins**: android-application, kotlin-compose, kotlin-ksp
- **compileSdk**: 36 (Android 15)
- **minSdk**: 28 (Android 9)
- **targetSdk**: 36 (Android 15)
- **Java compatibility**: VERSION_11
- **Compose enabled**: true
- **KSP dependency processing** for Room and Hilt annotation processing

### gradle.properties
```properties
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
```

## 🔌 Key Design Patterns

### MVVM Pattern
- **ViewModel** manages UI state via `StateFlow`
- **UiState** is a single immutable data class
- **UiEvent** is a sealed class for all user interactions
- **Screen** composables are stateless and reactive

### Repository Pattern
- Single source of truth for data access
- Abstracts data sources (Room database)
- Returns `Flow<T>` for reactive updates
- Handles all business logic

### Dependency Injection with Hilt
```kotlin
@HiltAndroidApp
class NotelyApplication : Application()

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel()

@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

### Navigation with Compose
```kotlin
sealed class Screen {
    object NoteList : Screen()
    data class NoteDetail(val noteId: Int) : Screen()
}

NavHost(navController, Screen.NoteList) {
    composable<Screen.NoteList> { ListNoteScreen() }
    composable<Screen.NoteDetail> { NoteDetailScreen() }
}
```

## 📝 Development Workflow

### Adding a New Feature

1. **Define Domain Model** (`domain/model/`)
   ```kotlin
   data class Note(
       val id: Int = 0,
       val title: String = "",
       val content: String = "",
       val createdAt: Long = System.currentTimeMillis(),
       val updatedAt: Long = System.currentTimeMillis(),
       val color: Int = 0xFFFFFF
   )
   ```

2. **Create Repository Interface** (`domain/repository/`)
   ```kotlin
   interface NoteRepository {
       fun getAllNotes(): Flow<List<Note>>
       suspend fun insertNote(note: Note)
   }
   ```

3. **Implement Data Access** (`data/local/`)
   - Create Room entity
   - Create DAO interface with Flow-based operations
   - Create mapper for entity conversion
   - Implement repository in `data/repository/`

4. **Build UI** (`ui/screens/`)
   - Create ViewModel with StateFlow for UI state
   - Define UiState and UiEvent sealed classes
   - Build composables as stateless functions
   - Use `collectAsState()` to bind state to UI

5. **Write Tests**
   - Mock repository dependencies
   - Test ViewModel state changes
   - Use Turbine to collect and verify Flow emissions

## 🎯 Current Features

✅ Note list display (structure in place, UI TBD)
✅ Local database persistence with Room
✅ Reactive updates via Flow/StateFlow
✅ Dependency injection with Hilt
✅ MVVM architecture implemented
✅ Navigation structure defined
⏳ Note creation screen (TODO)
⏳ Note editing screen (TODO)
⏳ Note detail screen (TODO)
⏳ UI component styling (TODO)

## 🔧 Troubleshooting

### KSP Compilation Issues
Ensure `ksp` version matches Kotlin version for AGP 9.0 compatibility. Currently using:
- Kotlin 2.3.21
- KSP 2.3.8
- AGP 9.0.1

### Missing Generated Code
If you see unresolved references to generated Hilt/Room code:
```bash
./gradlew clean
./gradlew :app:kspDebugKotlin  # Force KSP code generation
./gradlew :app:assembleDebug
```

## 📚 Project Structure Best Practices

- **Feature Isolation**: Each feature owns its domain, data, and UI layers
- **No Circular Dependencies**: Data layer doesn't depend on UI layer
- **Interface-based Design**: Use repository interfaces, not concrete implementations
- **Flow for Streams**: All data sources return `Flow<T>` for reactive updates
- **Immutable Models**: Domain and UI state should be immutable data classes
- **Sealed Classes for Events**: Use sealed classes for type-safe event handling
