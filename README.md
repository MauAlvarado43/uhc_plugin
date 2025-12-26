# UHC — Minecraft Plugin

This repository contains the UHC (Ultra Hardcore) plugin for Minecraft developed in Java (Maven). The plugin provides basic functionality to run UHC matches on a Paper server.

## Project structure

Top-level layout (important files and folders):

```text
pom.xml
README.md
bin/                # build, move, setup and test scripts (Windows .bat)
src/
  main/
    java/
      vch/uhc/
        commands/   # command handlers (MainCommandHandler, PlayerCommandHandler, SettingsCommandHandler, TeamCommandHandler)
        listeners/  # event listeners (PlayerJoinListener, ChatListener, etc.)
        managers/   # managers (UHCManager, TeamManager, PlayerManager)
        models/     # data models (Player, Team)
        items/      # custom item definitions and recipes
        expansions/ # PlaceholderAPI expansions
        misc/       # utilities, base classes, helpers
    resources/
      plugin.yml    # plugin descriptor (commands, version, main class)
target/             # Maven build output
test_server/        # Local server files for testing (contains a server jar and configs)
```

## Requirements

- Java JDK 17 or newer
- Maven 3.6+
- Paper server compatible with the configured API version

## Quick start (Windows)

### Option 1: Full automation

Run the master script to setup, build and start the server all at once:

```batch
.\bin\start.bat
```

### Option 2: Step by step

1. Setup the test server (downloads Paper and dependencies):

```batch
.\bin\setup.bat
```

2. Build the plugin:

```batch
.\bin\build.bat
```

3. Run the test server (copies plugin JAR and starts the server):

```batch
.\bin\run.bat
```

### Option 3: Maven directly

```batch
mvn clean package
REM result: target/uhc-<version>.jar
```

### Running tests

```batch
.\bin\test.bat
```

Or with Maven:

```batch
mvn test
```

## Plugin commands

All commands are registered under the root command `/uhc` (permission `uhc.use`). Subcommands and permissions are listed below.

- /uhc info
  - Description: Show UHC information
  - Usage: `/uhc info`
  - Permission: `uhc.info`

- /uhc start
  - Description: Start the UHC game
  - Usage: `/uhc start`
  - Permission: `uhc.start`

- /uhc cancel
  - Description: Cancel the UHC game
  - Usage: `/uhc cancel`
  - Permission: `uhc.cancel`

- /uhc pause
  - Description: Pause the UHC game
  - Usage: `/uhc pause`
  - Permission: `uhc.pause`

- /uhc join
  - Description: Join the UHC game (players only)
  - Usage: `/uhc join`
  - Permission: `uhc.join`

- /uhc leave
  - Description: Leave the UHC game (players only)
  - Usage: `/uhc leave`
  - Permission: `uhc.leave`

- /uhc players
  - Description: Player management commands (admin)
  - Usage: `/uhc players <list|setLives|setHealth|revive>`
  - Permission: `uhc.players`
  - Children:
    - `list` — `/uhc players list` — Lists tracked players and their lives (`uhc.players.list`)
    - `setLives` — `/uhc players setLives <player> <lives>` — Set player lives (`uhc.players.setLives`)
    - `setHealth` — `/uhc players setHealth <player> <health>` — Set player's max health (`uhc.players.setHealth`)
    - `revive` — `/uhc players revive <player>` — Revive a player and teleport to spawn (`uhc.players.revive`)

- /uhc settings
  - Description: Change UHC settings (admin)
  - Usage: `/uhc settings <subcommand> <setting> <value>`
  - Permission: `uhc.settings`
  - Subcommand: `set` — `/uhc settings set <setting> <value>` — Sets various numeric and enum settings (`uhc.settings.set`)

- /uhc team
  - Description: Team management commands (partial implementation)
  - Usage: `/uhc team <create|add|remove|rename|leave>`
  - Permission: `uhc.team`
  - Notes: Only a skeleton exists for team handling; implementation may be partial.

Examples:

```text
/uhc info
/uhc start
/uhc settings set PVP true
```

## Development

Run tests and package with Maven:

```batch
mvn test
mvn package
```

Or use the convenience scripts:

```batch
.\bin\test.bat
.\bin\build.bat
```

Use the [run.bat](bin/run.bat) script to launch the local test server and verify the plugin loads correctly.

## Troubleshooting

- Plugin does not appear in `plugins/` after server start: check that the JAR contains `plugin.yml` (see `target/classes/plugin.yml`).
- Errors during plugin enable: inspect the server console and `logs/latest.log` for stack traces and missing dependency messages (the plugin declares depend on `TAB` and `PlaceholderAPI`).
- Permissions/commands not working: verify permissions defined in `plugin.yml` and that your permissions plugin (if used) recognizes them.
- You can download `PlugmanX` to reload the plugin without restarting the server.

## Notes

- The `plugin.yml` file in `src/main/resources` is the authoritative source for commands and permissions. If you change commands or permissions, update that file and recompile.

### Recomended settings for TAB plugin

Set the following in `plugins/TAB/groups.yml` to show UHC player info in the tab list:
```yaml
_DEFAULT_:
  tabprefix: "%uchteam% %uchplayerlives%"
  tagprefix: ""
  customtabname: "%uchplayer%"
  tabsuffix: " %uchplayerhealth%"
  tagsuffix: ""
```

Disable the default health display to avoid conflicts in `plugins/TAB/config.yml`:
```yaml
playerlist-objective:
  enabled: false # This disables the default health display
  value: "%health%"
  fancy-value: "&7Ping: %ping%"
  title: "TAB"
  render-type: HEARTS
  disable-condition: '%world%=disabledworld'
```