# UHC Nopal — Ultra Hardcore Plugin

Welcome to the comprehensive documentation for **UHC Nopal**, the ultimate Ultra Hardcore solution for modern Minecraft servers. This plugin simplifies the management of competitive matches with automated game cycles, custom modes, and robust teaming systems.

## 🏁 For Administrators (Usage)

*Get your game up and running in minutes.*

- [**Setup & Installation**](docs/usage/setup.md): Requirements and initial setup.
- [**Configuration Guide**](docs/usage/configuration.md): Explaining every setting and toggle.
- [**Command Reference**](docs/usage/commands.md): A full list of `/uhc` and `/team` commands.
- [**Gameplay Mechanics**](docs/usage/gameplay.md): Distribution algorithms, death logic, and special events.

## 🛠 For Developers (Technical)

*Deep dive into how the plugin works under the hood.*

- [**Architecture Overview**](docs/technical/architecture.md): Patterns, singleton hub, and logic flow.
- [**Development & Testing**](docs/technical/development.md): Build automation, JUnit protocols, and scripts.
- [**Systems & Managers**](docs/technical/managers.md): Detailed registry of every subsystem.
- [**Persistence & Backups**](docs/technical/managers.md#backupmanager): Data integrity and recovery logic.

---

### Key Features

- **Automated Game Cycle**: From world preparation and player distribution to victory announcements.
- **Dynamic World Border**: Highly configurable shrinking border (Gradual, Instant, or Threshold-based).
- **Proximity Teaming**: Optional "In-Game" team mode where players form alliances by being close to each other.
- **Resource Rush**: A competitive game mode where teams race to gather specific items.
- **Localization**: Full support for English and Spanish (extensible via `.properties`).
- **Persistence**: Game settings are stored in `settings.json` for easy manual editing and consistency.

### Quick Links

- **[Issue Tracker](https://github.com/MauAlvarado43/uhc_plugin/issues)**
- **[Report a Bug](docs/usage/setup.md#troubleshooting)**

*Developed with ❤️ for the Minecraft Competitive Community.*
