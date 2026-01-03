# Command & Permission Reference

UHC Nopal uses a hierarchical command system. Most administrative actions are grouped under `/uhc`.

## 🛠 Administrative Commands (`uhc.admin`)

These commands require the `uhc.admin` permission.

| Command | Usage | Description |
| :--- | :--- | :--- |
| `/uhc start` | `/uhc start` | Begins the 10-second countdown to distribute players and start the game. |
| `/uhc pause` | `/uhc pause` | Freezes the game timer, prevents the border from moving, and disables all game logic. |
| `/uhc resume` | `/uhc resume` | Restarts the ticker from exactly where it was paused. |
| `/uhc cancel` | `/uhc cancel` | Resets the game state to 'NONE'. Teleports players to spawn (if configured). |
| `/uhc reload` | `/uhc reload` | Reloads all configurations and message files. |
| `/uhc settings` | `/uhc settings [set <key> <val>]` | Opens the settings GUI or sets a value directly via text. |

## 👥 Team Management (`uhc.player`)

Players can manage their own teams unless the game is in 'In-Game' teaming mode.

| Command | Usage | Description |
| :--- | :--- | :--- |
| `/team create` | `/team create <name>` | Creates a new team and makes the sender the leader. |
| `/team join` | `/team join <name>` | Requests to join an existing team. Request is automatically accepted (configurable). |
| `/team leave` | `/team leave` | Leaves the current team. If sender was the leader, a new leader is promoted. |
| `/team invite` | `/team invite <player>` | Sends a join request to another player. |
| `/team rename` | `/team rename <new_name>` | (Leader only) Changes the display name of the team. |

## 🕹 Player Management (`uhc.admin.players`)

For real-time adjustments during the match.

| Command | Usage | Description |
| :--- | :--- | :--- |
| `/uhc players list` | `/uhc players list` | Shows a status report of all players (Health, Lives, Team). |
| `/uhc players setlives` | `/uhc players setlives <p> <n>` | Adds or removes lives from a specific player. |
| `/uhc players revive` | `/uhc players revive <p>` | Teleports a dead/spectating player back into the game with 1 life. |
| `/uhc players sethealth` | `/uhc players sethealth <p> <n>` | Modifies the current max health attribute of a player. |

## 📑 Backup System (`uhc.admin.backup`)

| Command | Usage | Description |
| :--- | :--- | :--- |
| `/uhc backup save` | `/uhc backup save` | Forces an immediate save of the current game state to `backup.json`. |
| `/uhc backup restore` | `/uhc backup restore` | Overwrites the current game state with the data from the backup file. |
| `/uhc backup clear` | `/uhc backup clear` | Deletes the current backup file. |
