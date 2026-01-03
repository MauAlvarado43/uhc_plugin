# Gameplay Mechanics & Flow

This document details the unique mechanics of UHC Nopal and how a typical match progresses.

## 1. The Perimeter Spawn Algorithm

Unlike vanilla spawns, UHC Nopal uses a mathematical distribution to ensure no player has an unfair distance advantage.

- **Teams 1-4**: Placed at the corners and secondary midpoints.
- **Teams 5+**: Evenly distributed along the world border perimeter.
- **Safety Check**: Each player/team is spawned on a 3x3 bedrock platform to avoid falling into lava or being trapped in blocks.

## 2. In-Game Teaming Mode

If `teams.mode` is set to `IN_GAME`, the `/team` commands are disabled.

- **How it works**: Players start solo. If two players stand within 5 blocks of each other for a cumulative period of 30 seconds (configurable), they are automatically placed on a team.
- **Dynamic**: This adds a social layer where players must decide whom to trust on the fly.

## 3. World Border Phases

The border doesn't just "shrink"; it follows specific phases:

- **Phase 1 (Preparation)**: Border is static. Players gather initial resources.
- **Phase 2 (Shrinking)**: The border moves towards (0,0) at a set speed (blocks per minute).
- **Phase 3 (Final Showdown)**: The border stops at its `final_size` (usually 50x50), forcing players into a small arena.

## 4. Special Event: Skin Shuffle

If enabled, this mode periodically (e.g., every 10 minutes) randomizes the skins of all alive players.

- **Mechanism**: All players might look like Steve, or like each other.
- **Gameplay Impact**: Makes it difficult to identify known "pro" players or specific teammates from a distance, favoring stealth and careful communication.

## 5. Global Buffs Program

At the mid-game mark (configured as `buffs_time`), the following happens:

1. **Health Expansion**: Every alive player gets a permanent increase of +2 to +10 hearts (configurable).
2. **Resistance**: A temporary Resistance II effect is applied to allow players to survive the sudden health transition.
3. **Sound/Visuals**: A deep horn sound plays, and a title appears on screen.

## 6. Death & Spectating

- **Lives System**: By default, players have 1 life. If they die, they are switched to Gamemode Spectator.
- **Spectator Logic**: Spectators can see other players but cannot interact with blocks or inventories. They are restricted to stays within the world border.
- **Revival**: An admin can manually revive a player, which removes them from spectator mode and teleports them to a safe location inside the current border.
