## Usage

### Generate Dependency Information

```bash
./gradlew :app-desktop:exportLibraryDefinitions -PaboutLibraries.exportPath=src/commonMain/composeResources/files/
```

### Run Wasm app

```
./gradlew :app-desktop:wasmJsRun   
```