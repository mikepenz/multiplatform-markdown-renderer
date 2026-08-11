## Sample app

### Generate Dependency Information

```bash
./gradlew :sample:shared:exportLibraryDefinitions
./gradlew :sample:desktop:exportLibraryDefinitions
./gradlew :sample:web:exportLibraryDefinitions

```

### Generate reference images for tests

```bash
./gradlew :sample:android:recordPaparazzi
```

### Verify snapshots against the reference images

```bash
./gradlew :sample:android:verifyPaparazzi
```

### Refresh the README showcase art

```bash
./gradlew :sample:android:recordPaparazzi :sample:android:copyReadmeArt
```

### Run Android app

```bash
./gradlew :sample:android:installDebug
```

### Run Desktop app

```bash
./gradlew :sample:desktop:run
```

### Run Wasm app

```bash
./gradlew :sample:web:wasmJsBrowserDevelopmentRun
```

### Run iOS app

- Set up an
  environment - https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-setup.html
- Install Kotlin Multiplatform Mobile
  plugin - https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform
- Open the project in IntelliJ IDEA / Android Studio
- Add a new run configuration for the iOS Application
- Run the iOS Application
