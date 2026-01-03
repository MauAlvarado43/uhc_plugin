# Systems & Managers Registry

The logic of UHC Nopal is divided into specific managers, each responsible for a distinct pillar of the gameplay experience.

## Game Logic

### `UHCManager`

The orchestrator. It manages the global `GameState` and coordinates transitions between them.

- **Responsibilities**: Victory checking, game start/cancel sequences, and high-level rules enforcement.
- **Key Method**: `checkWinCondition()` runs every tick to evaluate if a single team or player remains.

### `GameTimerManager`

The engine that drives time-based events.

- **Internal Ticker**: Runs a `BukkitTask` every 20 ticks (1 second).
- **Scheduled Events**:
  - **PvP Activation**: Disables/Enables damage between players based on configured timing.
  - **Global Buffs**: Triggers health expansions and resistance effects.
  - **Skin Shuffle**: Coordinates with `SkinManager` at specific intervals.
  - **Time Tracking**: Maintains `elapsedHours`, `minutes`, and `seconds` for display.

### `WorldManager`

Handles the physical environment and spatial logic.

- **Spawn Algorithm**: Calculates points along a circular or square perimeter based on the number of teams.
- **Border Management**: Interfaces with the Bukkit `WorldBorder` API. Supports instant jumps or smooth shrinking.
- **Safety**: Includes logic to generate bedrock platforms and clear surrounding blocks to prevent players from spawning inside walls.

---

## Player & Social

### `PlayerManager`

The database of all participants.

- **Tracking**: Maps `UUID`s to `UHCPlayer` objects.
- **Persistence**: Handles data even if a player is offline, ensuring lives and stats are maintained.

### `TeamManager`

Manages the competitive group structure.

- **Formation**: Handles team creation, joining, and renaming.
- **Leadership**: Implements a "Leader" system where specific members have management rights over the team.
- **Proximity Logic**: Contains the math for the "In-Game Teaming" mode, where players are automatically teamed if they remain within a specific radius of each other.

---

## Interface & Localization

### `ScoreboardManager`

The primary visual feedback system.

- **Optimization**: Clears and redraws scores only when necessary to minimize flicker.
- **Dynamic Content**: Displays real-time team health, remaining time, world border distance, and Resource Rush progress.

### `LanguageManager`

The localization engine.

- **Multi-Source**: can load messages from the plugin's internal JAR or from a physical file in the `lang/` folder.
- **Formatting**: Supports `java.text.MessageFormat` for complex variables like `{0}`, `{1}`.
- **Hot Reload**: Allows admins to update messages without restarting the server.

---

## Utilities

### `BackupManager`

The safety net for long games.

- **JSON Serialization**: converts the entire state of managers and players into a readable JSON file.
- **Auto-Save**: Triggers periodically during the game.
- **Recovery**: On plugin enable, if a "crash" is detected, it offers to restore the previous state from `backup.json`.
