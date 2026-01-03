# Configuration Deep Dive

UHC Nopal can be configured in two ways: via the `settings.json` file or through the in-game GUI settings.

## 1. In-Game Settings Menu

Access the menu with `/uhc settings` or `/settings`. This GUI allows you to change values on the fly without touching the file system. All changes made in the GUI are automatically persisted to `settings.json`.

### Configuration Categories

- **📅 Timing**: Set the exact duration for the match, agreement phase, and buff triggers.
- **🛡️ PvP Options**: Toggle friendly fire and the duration of the initial pacifist period.
- **🌎 Border Logic**: Choose between "Gradual" (shrinks throughout the game) or "Threshold" (waits for a specific time, then crashes in).
- **💕 Health Buffs**: Configure how many extra hearts players get and when.
- **🎭 Skin Shuffle**: Enabled a psychological warfare mode where everyone's skin changes periodically.

## 2. Configuration File (`settings.json`)

Unlike traditional Bukkit plugins, UHC Nopal uses a single JSON file for its settings. This ensures better data structured and compatibility with modern web-based monitoring tools.

### Key Parameter Examples

```json
{
  "maxWorldSize": 1000,
  "minWorldSize": 100,
  "gameMinutes": 60,
  "buffsEnabled": true,
  "extraHearts": 10.0,
  "skinShuffleMinutes": 5
}
```

## 3. Localization & Messaging

While main settings are JSON, localization still uses standard Java `.properties` files for ease of entry. These are managed in `plugins/UHC-Nopal/lang/`.

### Customizing Messages

1. Open `messages_en.properties`.
2. Find the key you want to change (e.g., `victory_title`).
3. Save the file and run `/uhc reload`.

### Color Codes

The plugin uses standard Minecraft color codes (`&` or `§`) and supports MiniMessage formatting (e.g., `<red>text</red>`) in certain areas.

## 4. Game Modes

### Resource Rush

To enable this mode, set `game_mode: RESOURCE_RUSH` in the settings. You must then provide a list of items that players need to find.

- **Victory Condition**: First team to have all required items in their joint inventories wins immediately.
