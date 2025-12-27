package vch.uhc.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;

import vch.uhc.UHC;
import vch.uhc.misc.BaseItem;
import vch.uhc.misc.Messages;
import vch.uhc.misc.enums.GameMode;
import vch.uhc.misc.enums.TeamMode;

/**
 * Handler for the /uhc settings command. Allows administrators to configure
 * various game parameters through commands.
 */
public class SettingsCommandHandler {

    /**
     * Executes settings-related subcommands.
     *
     * @param sender The command sender
     * @param args The command arguments
     * @return true if handled, false otherwise
     */
    public static boolean onSettingsCommand(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(Messages.SETTINGS_SPECIFY_SUBCOMMAND());
            return false;
        }

        if (!args[1].equalsIgnoreCase("set")) {
            sender.sendMessage(Messages.SETTINGS_UNKNOWN_SUBCOMMAND());
            return false;
        }

        if (args.length < 4) {
            sender.sendMessage(Messages.SETTINGS_SPECIFY_VALUE());
            return false;
        }

        String setting = args[2];
        String value = args[3];

        switch (setting.toLowerCase()) {

            case "gamemode" -> {
                // Set the game mode (PVD, PVP, ResourceRush)
                Map<String, GameMode> gameModes = Map.of(
                        "pvd", GameMode.PVD,
                        "pvp", GameMode.PVP,
                        "resourcerush", GameMode.RESOURCE_RUSH
                );

                GameMode gameMode = gameModes.get(value.toLowerCase());
                if (gameMode == null) {
                    sender.sendMessage(Messages.SETTINGS_UNKNOWN_SUBCOMMAND());
                    return false;
                }

                UHC.getPlugin().getSettings().setGameMode(gameMode);
                sender.sendMessage(Messages.SETTINGS_TEAM_MODE_SET(gameMode.toString()));
            }

            case "teammode" -> {
                // Set the team formation mode (Auto, Manual, InGame)
                Map<String, TeamMode> teamModes = Map.of(
                        "auto", TeamMode.AUTO,
                        "manual", TeamMode.MANUAL,
                        "ingame", TeamMode.IN_GAME
                );

                TeamMode teamMode = teamModes.get(value.toLowerCase());
                if (teamMode == null) {
                    sender.sendMessage(Messages.SETTINGS_TEAM_MODE_UNKNOWN());
                    return false;
                }

                UHC.getPlugin().getSettings().setTeamMode(teamMode);
                sender.sendMessage(Messages.SETTINGS_TEAM_MODE_SET(teamMode.toString()));
            }

            case "teamsize" -> {
                // Set the maximum size for teams
                try {
                    int teamSize = Integer.parseInt(value);
                    UHC.getPlugin().getSettings().setTeamSize(teamSize);
                    sender.sendMessage(Messages.SETTINGS_TEAM_SIZE_SET(teamSize));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_TEAM_SIZE_INVALID());
                }
            }

            case "playerlives" -> {
                // Set the number of lives each player starts with
                try {
                    int playerLives = Integer.parseInt(value);
                    UHC.getPlugin().getSettings().setPlayerLives(playerLives);
                    sender.sendMessage(Messages.SETTINGS_PLAYER_LIVES_SET(playerLives));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_PLAYER_LIVES_INVALID());
                }
            }

            case "maxworldsize" -> {
                // Set the initial world border size
                try {
                    int maxWorldSize = Integer.parseInt(value);
                    UHC.getPlugin().getSettings().setMaxWorldSize(maxWorldSize);
                    sender.sendMessage(Messages.SETTINGS_MAX_WORLD_SIZE_SET(maxWorldSize));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_MAX_WORLD_SIZE_INVALID());
                }
            }

            case "minworldsize" -> {
                // Set the final (minimum) world border size
                try {
                    int minWorldSize = Integer.parseInt(value);
                    UHC.getPlugin().getSettings().setMinWorldSize(minWorldSize);
                    sender.sendMessage(Messages.SETTINGS_MIN_WORLD_SIZE_SET(minWorldSize));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_MIN_WORLD_SIZE_INVALID());
                }
            }

            case "gamehours", "gameminutes", "gameseconds" -> {
                // Handle game time settings
                try {
                    int timeVal = Integer.parseInt(value);
                    if (setting.toLowerCase().contains("hours")) {
                        UHC.getPlugin().getSettings().setGameHours(timeVal);
                        sender.sendMessage(Messages.SETTINGS_HOURS_SET(timeVal));
                    } else if (setting.toLowerCase().contains("minutes")) {
                        UHC.getPlugin().getSettings().setGameMinutes(timeVal);
                        sender.sendMessage(Messages.SETTINGS_MINUTES_SET(timeVal));
                    } else {
                        UHC.getPlugin().getSettings().setGameSeconds(timeVal);
                        sender.sendMessage(Messages.SETTINGS_SECONDS_SET(timeVal));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_HOURS_INVALID());
                }
            }

            case "agreementhours", "agreementminutes", "agreementseconds" -> {
                // Handle PvP agreement (Pact of Gentlemen) time settings
                try {
                    int timeVal = Integer.parseInt(value);
                    if (setting.toLowerCase().contains("hours")) {
                        UHC.getPlugin().getSettings().setAgreementHours(timeVal);
                        sender.sendMessage(Messages.SETTINGS_AGREEMENT_HOURS_SET(timeVal));
                    } else if (setting.toLowerCase().contains("minutes")) {
                        UHC.getPlugin().getSettings().setAgreementMinutes(timeVal);
                        sender.sendMessage(Messages.SETTINGS_AGREEMENT_MINUTES_SET(timeVal));
                    } else {
                        UHC.getPlugin().getSettings().setAgreementSeconds(timeVal);
                        sender.sendMessage(Messages.SETTINGS_AGREEMENT_SECONDS_SET(timeVal));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_HOURS_INVALID());
                }
            }

            case "minworldborderhours", "minworldborderminutes", "minworldborderseconds" -> {
                // Handle world border shrinking completion time settings
                try {
                    int timeVal = Integer.parseInt(value);
                    if (setting.toLowerCase().contains("hours")) {
                        UHC.getPlugin().getSettings().setMinWorldBorderHours(timeVal);
                        sender.sendMessage(Messages.SETTINGS_MIN_BORDER_HOURS_SET(timeVal));
                    } else if (setting.toLowerCase().contains("minutes")) {
                        UHC.getPlugin().getSettings().setMinWorldBorderMinutes(timeVal);
                        sender.sendMessage(Messages.SETTINGS_MIN_BORDER_MINUTES_SET(timeVal));
                    } else {
                        UHC.getPlugin().getSettings().setMinWorldBorderSeconds(timeVal);
                        sender.sendMessage(Messages.SETTINGS_MIN_BORDER_SECONDS_SET(timeVal));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_HOURS_INVALID());
                }
            }

            case "maxteamingamehours", "maxteamingameminutes", "maxteamingameseconds" -> {
                // Handle in-game team formation time limit
                try {
                    int timeVal = Integer.parseInt(value);
                    if (setting.toLowerCase().contains("hours")) {
                        UHC.getPlugin().getSettings().setMaxTeamInGameHours(timeVal);
                        sender.sendMessage(Messages.SETTINGS_MAX_TEAM_HOURS_SET(timeVal));
                    } else if (setting.toLowerCase().contains("minutes")) {
                        UHC.getPlugin().getSettings().setMaxTeamInGameMinutes(timeVal);
                        sender.sendMessage(Messages.SETTINGS_MAX_TEAM_MINUTES_SET(timeVal));
                    } else {
                        UHC.getPlugin().getSettings().setMaxTeamInGameSeconds(timeVal);
                        sender.sendMessage(Messages.SETTINGS_MAX_TEAM_SECONDS_SET(timeVal));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_HOURS_INVALID());
                }
            }

            case "shulkerenabled" -> {
                // Enable or disable randomized Shulker box delivery
                boolean shulkerEnabled = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("enabled");
                UHC.getPlugin().getSettings().setShulkerEnabled(shulkerEnabled);
                String status = shulkerEnabled ? Messages.INFO_ENABLED() : Messages.INFO_DISABLED();
                sender.sendMessage(Messages.SETTINGS_SHULKER_SET(status));
            }

            case "locatorbarenabled" -> {
                // Enable or disable the nearby player locator HUD element
                boolean locatorBarEnabled = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("enabled");
                UHC.getPlugin().getSettings().setLocatorBarEnabled(locatorBarEnabled);
                String status = locatorBarEnabled ? Messages.INFO_ENABLED() : Messages.INFO_DISABLED();
                sender.sendMessage(Messages.SETTINGS_LOCATOR_BAR_SET(status));
            }

            case "recipe" -> {
                // Enable or disable specific custom crafting recipes
                if (args.length < 5) {
                    sender.sendMessage(Messages.SETTINGS_RECIPE_SPECIFY());
                    return false;
                }

                String recipeName = args[3];
                String recipeValue = args[4];

                boolean enabled;
                if (recipeValue.equalsIgnoreCase("enabled") || recipeValue.equalsIgnoreCase("true")) {
                    enabled = true;
                } else if (recipeValue.equalsIgnoreCase("disabled") || recipeValue.equalsIgnoreCase("false")) {
                    enabled = false;
                } else {
                    sender.sendMessage(Messages.SETTINGS_RECIPE_INVALID_VALUE());
                    return false;
                }

                BaseItem itemEntry = UHC.getPlugin().getSettings().getItems().stream()
                        .filter(item -> item.getKey().getKey().equalsIgnoreCase(recipeName))
                        .findFirst().orElse(null);

                if (itemEntry == null) {
                    sender.sendMessage(Messages.SETTINGS_RECIPE_UNKNOWN());
                    return false;
                }

                if (enabled) {
                    itemEntry.enable();
                } else {
                    itemEntry.disable();
                }

                sender.sendMessage(Messages.SETTINGS_RECIPE_SET(recipeName, enabled));
            }

            default ->
                sender.sendMessage(Messages.SETTINGS_UNKNOWN());
        }

        return true;
    }
}
