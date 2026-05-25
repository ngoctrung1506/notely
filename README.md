# Notely - Note Taking Android App

Notely is a simple note-taking application built with **Kotlin** and **Jetpack Compose** following the **MVVM** architecture with **Repository Pattern** for clean data management.

## 📋 Project Architecture

```
app/src/main/java/com/app/notely/
├── core/
│   ├── di/                # Hilt Dependency Injection modules
│   ├── navigation/        # Navigation routes (sealed class) & graph
│   ├── network/           # Network clients, Retrofit services, API models
│   ├── util/              # Utility functions (date formatting, constants)
│   ├── extension/         # Compose Modifier extensions (noRippleClickable)
│   └── mock/              # Test helpers and mock data providers
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAO interface with Flow-based CRUD
│   │   ├── entity/        # Room entity classes (NoteEntity)
│   │   └── mapper/        # Entity ↔ Domain model conversion
│   ├── remote/            # Remote API clients, DTOs, and network mappers
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

| Technology | Purpose |
|-----------|----------|
| **Kotlin** | Programming language |
| **AGP** | Android Gradle Plugin |
| **Gradle** | Build system |
| **Jetpack Compose** | Modern UI framework |
| **Material3** | Design system |
| **Room** | Local database (SQLite) |
| **Hilt** | Dependency injection |
| **KSP** | Kotlin Symbol Processing (annotation processing) |
| **Navigation Compose** | Composable-based navigation |
| **Coroutines** | Async programming |
| **Flow/StateFlow** | Reactive streams |
| **DataStore** | Preferences storage |
| **MockK** | Mocking library for tests |
| **Turbine** | Flow testing utility |
| **Firebase Firestore** | Cloud synchronization |

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
Shared infrastructure: dependency injection modules, navigation routes, utilities, extensions, network clients (Retrofit/HTTP), and test helpers/mocks located under `core/mock`.

### Data Layer (`data/`)
Data access and synchronization: local persistence with Room (entities, DAOs, mappers), plus remote data sources under `data/remote` for API clients, DTOs, and network-to-domain mapping. The `repository` layer mediates between local and remote sources.

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

**Status**: Testing strategy and test suite are not yet developed. The project has dependencies configured (MockK 1.13.8, Turbine 1.0.0, Coroutines Test) but comprehensive unit and instrumented tests are not implemented.

Once developed, the testing strategy will include:

### Unit Tests (JUnit 4 + MockK)
- **ViewModels**: Testing state management and event handling with StateFlow collection
- **Repository Layer**: Testing data access and Flow emission logic
- **Domain Models**: Testing business logic and model validation

### Test Framework Details
- **MockK**: Mocking framework for Kotlin dependencies
- **Turbine**: Flow testing utility for collecting and asserting emissions
- **Coroutines Test**: Dispatcher management for async testing

### Running Tests (Once Implemented)
```bash
# Run unit tests
./gradlew :app:testDebugUnitTest

# Run instrumented tests
./gradlew :app:connectedAndroidTest
```

## 📦 Build Configuration

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

✅ Note list display with MVVM architecture
✅ Local database persistence with Room
✅ Reactive updates via Flow/StateFlow
✅ Dependency injection with Hilt
✅ Navigation structure with Compose Navigation
✅ Cloud synchronization with Firebase Firestore
✅ Responsive UI with Material 3 design
✅ Note detail, creation, editing, and deletion
✅ Search notes functionality by title or content
✅ Sort notes by date, title
✅ View tags for notes



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
