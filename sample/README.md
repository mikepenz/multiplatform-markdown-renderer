## Sample app

### Generate Dependency Information

```bash
./gradlew :sample:exportLibraryDefinitions -PaboutLibraries.exportPath=src/commonMain/composeResources/files/
```

### Run Android app

```
./gradlew :sample:installDebug
```

### Run Desktop app

```
./gradlew :sample:run
```

### Run Wasm app

```
./gradlew :sample:wasmJsRun
```

### Run iOS app

- Set up an environment - https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-setup.html
- Install Kotlin Multiplatform Mobile plugin - https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile
- Open the project in IntelliJ IDEA / Android Studio
- Add a new run configuration for the iOS Application
- Run the iOS Application
