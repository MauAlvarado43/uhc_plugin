# Plugin Architecture

UHC Nopal is designed with a highly modular architecture that prioritizes scalability, testability, and clear separation of concerns.

## Project Structure

```text
src/
  main/
    java/
      vch/uhc/
        commands/   # Command handlers grouped by category
        listeners/  # Event listeners (Game, Player, UI, Chat)
        managers/   # Core logic managers
        models/     # Data entities (UHCPlayer, UHCTeam, GameStats)
        items/      # Custom items and crafting recipes
        expansions/ # PlaceholderAPI integration
        misc/       # LanguageManager, Settings, and Constants
    resources/
      lang/         # .properties files for localization
      plugin.yml    # Bukkit plugin descriptor
      settings.json # Main configuration file (JSON format for structured data)
bin/                # Windows automation scripts (.bat)
test_server/        # Local sandbox for rapid testing
```

## Design Philosophy

The plugin follows three core principles:

1. **Decoupling**: Business logic (how the game works) is separated from external APIs (Minecraft server mechanics).
2. **Centralized Management**: Every major system has a dedicated "Manager" responsible for its state and operations.
3. **JSON Persistence**: Unlike standard plugins that use YAML, UHC Nopal uses JSON via GSON for settings and game state backups to ensure structure integrity and performance.

## Core Design Patterns

### 1. Manager-Listener Pattern

This is the backbone of the plugin.

- **Managers** (`vch.uhc.managers.*`) hold the "source of truth". For example, `PlayerManager` knows which players are alive, while `GameTimerManager` knows exactly how much time has passed.
- **Listeners** (`vch.uhc.listeners.*`) capture raw Minecraft events (like a player dying or a block breaking) and translate them into manager calls. This keeps listeners "thin" and easy to debug.

### 2. Singleton Hub (`UHC` Class)

The main `UHC` class acts as a service locator. It initializes all managers in a specific order to handle dependencies and provides public accessors for them.

```java
UHC plugin = UHC.getPlugin();
plugin.getWorldManager().shrinkBorder(5.0);
```

### 3. Entity Modeling

Data is represented by clean POJO (Plain Old Java Object) models in `vch.uhc.models`.

- **`UHCPlayer`**: Wraps a Bukkit `Player` and adds UHC-specific stats like lives, team affiliation, and elimination status.
- **`UHCTeam`**: Extends the concept of vanilla teams to support custom names, leadership, and proximity-based dynamics.

## Lifecycle of a Game Session

1. **Initialization**: On server start, `config.yml` is loaded, and the `LanguageManager` initializes the message cache.
2. **Preparation**: When `/uhc start` is called, `WorldManager` calculates the "Fair Spawn" perimeter.
3. **The Ticker**: `GameTimerManager` starts a recurring task (1-second intervals). This task is the "heartbeat" of the game, triggering every other system.
4. **Conclusion**: When victory conditions are met in `UHCManager`, the ticker stops, winners are broadcasted, and the server enters a "clean-up" state.

## Tech Stack

- **Language**: Java 21 (using modern features like Records and switch expressions).
- **Platform**: Paper/Spigot 1.21.1.
- **Library**: Adventure API (for mini-message and component support).
- **Communication**: Bungee/Velocity compatible (via internal messaging).
