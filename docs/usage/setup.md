# Setup & Installation Guide

Setting up UHC Nopal correctly is crucial for a smooth competitive experience. Follow these steps to prepare your server.

## 1. System Requirements

- **Hardware**: At least 4GB of RAM allocated to the server (UHC worlds generate many chunks quickly).
- **Java**: Java 21 or higher.
- **Server JAR**: [PaperMC](https://papermc.io/) is highly recommended due to its performance optimizations.

## 2. Installation Steps

1. Download the latest `uhc-nopal.jar`.
2. Place the JAR into the `/plugins` directory of your server.
3. Install the **Dependencies**:
    - **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)**: Required for scoreboard and formatting.
    - **[SkinsRestorer](https://skinsrestorer.net/)**: Only required if you want to use the "Skin Shuffle" mechanic.
4. Start the server. This will generate the `plugins/UHC-Nopal` folder and default configurations.

## 3. Initial Configuration

Navigate to `plugins/UHC-Nopal/settings.json`. The most important initial settings are provided in JSON format:

```json
{
  "brandName": "UHC Nopal",
  "teamSize": 2,
  "playerLives": 1,
  "maxWorldSize": 1000,
  "minWorldSize": 50,
  "shulkerEnabled": false
}
```

## 4. World Preparation

UHC games work best on a **freshly generated world**.

1. Stop the server.
2. Delete the `world`, `world_nether`, and `world_the_end` folders if they exist.
3. Start the server. UHC Nopal will automatically apply gamerules (no resource regeneration, etc.) when the game starts.

### Recommended Plugins for performance

- **Chunky**: Use this to pre-generate the world within your initial border size. This prevents lag spikes during the match.
  - Command: `/chunky radius 1000`, then `/chunky start`.

## 5. Verifying the installation

Once the server is up, run `/uhc info`. If the plugin is working, you should see a list of version info and current game settings in the chat.

## 6. Recomended settings for TAB plugin

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

## 7. Troubleshooting

- **Plugin doesn't load**: Check that the JAR is in the `/plugins` folder and that you have all required dependencies installed.
- **Dependency Errors**: Inspect the server console and `logs/latest.log` for missing dependency messages (specifically `TAB` and `PlaceholderAPI`).
- **Commands not working**: Ensure your permission manager (like LuckPerms) recognizes the `uhc.admin` and `uhc.player` permissions.
- **Recipe Conflicts**: If a custom recipe isn't appearing, check if another plugin is overriding it. You can force reload with `/uhc reload`.
