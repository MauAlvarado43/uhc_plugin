# Development & Testing

This guide covers the development workflow, build automation, and testing protocols for UHC Nopal.

## Development Workflow

The project uses **Maven** for dependency management and build automation. We also provide a set of convenience scripts for Windows environments to streamline common tasks.

### 1. Project Build

To compile the plugin and generate the JAR file:

```bash
mvn clean package
```

The resulting artifact will be located at `target/uhc-<version>.jar`.

### 2. Automated Scripts (Windows)

Found in the `bin/` directory, these scripts automate the local testing environment:

- `setup.bat`: Downloads the Paper server JAR and necessary dependencies (PlaceholderAPI, SkinsRestorer, etc.) into a `test_server/` folder.
- `build.bat`: Compiles the plugin and copies the JAR directly into the `test_server/plugins/` folder.
- `run.bat`: Launches the test server.
- `start.bat`: A master script that runs setup, build, and run in sequence.

## Testing Strategy

Testing is a first-class citizen in UHC Nopal. We utilize **JUnit 5** for the test runner and **AssertJ** for fluent assertions.

### Execution

To run the full test suite:

```bash
mvn test
```

Or via the shortcut:

```batch
.\bin\test.bat
```

### Coverage Areas

- **Algorithms**: Validation of the circular/perimeter spawn logic to ensure fair player distribution.
- **Game State**: Testing of transitions (e.g., from `PAUSED` to `IN_PROGRESS`) and win condition triggers.
- **Model Integrity**: Unit tests for `UHCPlayer` and `UHCTeam` to ensure data consistency during complex merges or eliminations.

## Troubleshooting Development

- **Missing Classes**: Ensure you have run `mvn install` to download all remote dependencies from JitPack and PaperMC repos.
- **Port Conflicts**: If using `run.bat`, ensure port `25565` is not occupied by another Minecraft server instance.
- **Hot Reloading**: While we support `/uhc reload` for config files, it is recommended to use **PlugmanX** or a full server restart when modifying core Java logic to avoid classloader leaks.
