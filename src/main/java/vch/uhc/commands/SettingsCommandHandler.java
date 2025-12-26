package vch.uhc.commands;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import vch.uhc.UHC;
import vch.uhc.misc.BaseItem;
import vch.uhc.misc.Settings;
import vch.uhc.misc.Messages;

public class SettingsCommandHandler {

    public static boolean onSettingsCommand(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(Messages.SETTINGS_SPECIFY_SUBCOMMAND());
            return false;
        }

        switch (args[1]) {
            case "set":

                if (args.length < 4) {
                    sender.sendMessage(Messages.SETTINGS_SPECIFY_VALUE());
                    return false;
                }

                String setting = args[2];
                String value = args[3];

                switch (setting) {

                    case "gameMode":

                        Map<String, Settings.GameMode> gameModes = Map.of(
                                "pvd", Settings.GameMode.PVD,
                                "pvp", Settings.GameMode.PVP,
                                "resourceRush", Settings.GameMode.RESOURCE_RUSH
                        );

                        Settings.GameMode gameMode = gameModes.get(value);
                        if (gameMode == null) {
                            sender.sendMessage(Messages.SETTINGS_UNKNOWN_SUBCOMMAND());
                            return false;
                        }

                        UHC.getPlugin().getSettings().setGameMode(gameMode);
                        sender.sendMessage(Messages.SETTINGS_TEAM_MODE_SET(gameMode.toString()));
                        break;

                    case "teamMode":

                        Map<String, Settings.TeamMode> teamModes = Map.of(
                                "auto", Settings.TeamMode.AUTO,
                                "manual", Settings.TeamMode.MANUAL,
                                "inGame", Settings.TeamMode.IN_GAME
                        );

                        Settings.TeamMode teamMode = teamModes.get(value);
                        if (teamMode == null) {
                            sender.sendMessage(ChatColor.RED + "Unknown team mode.");
                            return false;
                        }

                        UHC.getPlugin().getSettings().setTeamMode(teamMode);
                        sender.sendMessage(ChatColor.GREEN + "Team mode set to " + teamMode.toString() + ".");
                        break;

                    case "teamSize":
                        try {
                            int teamSize = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setTeamSize(teamSize);
                            sender.sendMessage(ChatColor.GREEN + "Team size set to " + teamSize + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid team size.");
                        }
                        break;

                    case "playerLives":
                        try {
                            int playerLives = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setPlayerLives(playerLives);
                            sender.sendMessage(ChatColor.GREEN + "Player lives set to " + playerLives + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid player lives.");
                        }
                        break;

                    case "maxWorldSize":
                        try {
                            int maxWorldSize = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMaxWorldSize(maxWorldSize);
                            sender.sendMessage(ChatColor.GREEN + "Max world size set to " + maxWorldSize + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid max world size.");
                        }
                        break;

                    case "minWorldSize":
                        try {
                            int minWorldSize = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMinWorldSize(minWorldSize);
                            sender.sendMessage(ChatColor.GREEN + "Min world size set to " + minWorldSize + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid min world size.");
                        }
                        break;

                    case "gameHours":
                        try {
                            int hours = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setGameHours(hours);
                            sender.sendMessage(ChatColor.GREEN + "Hours set to " + hours + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid hours.");
                        }
                        break;

                    case "gameMinutes":
                        try {
                            int minutes = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setGameMinutes(minutes);
                            sender.sendMessage(ChatColor.GREEN + "Minutes set to " + minutes + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid minutes.");
                        }
                        break;

                    case "gameSeconds":
                        try {
                            int seconds = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setGameSeconds(seconds);
                            sender.sendMessage(ChatColor.GREEN + "Seconds set to " + seconds + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid seconds.");
                        }
                        break;

                    case "agreementHours":
                        try {
                            int hours = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setAgreementHours(hours);
                            sender.sendMessage(ChatColor.GREEN + "Agreement hours set to " + hours + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid agreement hours.");
                        }
                        break;

                    case "agreementMinutes":
                        try {
                            int minutes = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setAgreementMinutes(minutes);
                            sender.sendMessage(ChatColor.GREEN + "Agreement minutes set to " + minutes + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid agreement minutes.");
                        }
                        break;

                    case "agreementSeconds":
                        try {
                            int seconds = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setAgreementSeconds(seconds);
                            sender.sendMessage(ChatColor.GREEN + "Agreement seconds set to " + seconds + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid agreement seconds.");
                        }
                        break;

                    case "minWorldBorderHours":
                        try {
                            int hours = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMinWorldBorderHours(hours);
                            sender.sendMessage(ChatColor.GREEN + "Min world border hours set to " + hours + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid min world border hours.");
                        }
                        break;

                    case "minWorldBorderMinutes":
                        try {
                            int minutes = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMinWorldBorderMinutes(minutes);
                            sender.sendMessage(ChatColor.GREEN + "Min world border minutes set to " + minutes + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid min world border minutes.");
                        }
                        break;

                    case "minWorldBorderSeconds":
                        try {
                            int seconds = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMinWorldBorderSeconds(seconds);
                            sender.sendMessage(ChatColor.GREEN + "Min world border seconds set to " + seconds + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid min world border seconds.");
                        }
                        break;

                    case "maxTeamInGameHours":
                        try {
                            int hours = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMaxTeamInGameHours(hours);
                            sender.sendMessage(ChatColor.GREEN + "Max team in game hours set to " + hours + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid max team in game hours.");
                        }
                        break;

                    case "maxTeamInGameMinutes":
                        try {
                            int minutes = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMaxTeamInGameMinutes(minutes);
                            sender.sendMessage(ChatColor.GREEN + "Max team in game minutes set to " + minutes + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid max team in game minutes.");
                        }
                        break;

                    case "maxTeamInGameSeconds":
                        try {
                            int seconds = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setMaxTeamInGameSeconds(seconds);
                            sender.sendMessage(ChatColor.GREEN + "Max team in game seconds set to " + seconds + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid max team in game seconds.");
                        }
                        break;

                    case "endPortalHours":
                        try {
                            int hours = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setEndPortalHours(hours);
                            sender.sendMessage(ChatColor.GREEN + "End portal activation hours set to " + hours + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid end portal hours.");
                        }
                        break;

                    case "endPortalMinutes":
                        try {
                            int minutes = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setEndPortalMinutes(minutes);
                            sender.sendMessage(ChatColor.GREEN + "End portal activation minutes set to " + minutes + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid end portal minutes.");
                        }
                        break;

                    case "endPortalSeconds":
                        try {
                            int seconds = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setEndPortalSeconds(seconds);
                            sender.sendMessage(ChatColor.GREEN + "End portal activation seconds set to " + seconds + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid end portal seconds.");
                        }
                        break;

                    case "shulkerEnabled":
                        boolean shulkerEnabled;
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("enabled")) {
                            shulkerEnabled = true;
                        } else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("disabled")) {
                            shulkerEnabled = false;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Invalid value. Use: true/false or enabled/disabled");
                            return false;
                        }
                        UHC.getPlugin().getSettings().setShulkerEnabled(shulkerEnabled);
                        sender.sendMessage(ChatColor.GREEN + "Shulker spawn set to " + (shulkerEnabled ? "enabled" : "disabled") + ".");
                        break;

                    case "shulkerHours":
                        try {
                            int hours = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setShulkerHours(hours);
                            sender.sendMessage(ChatColor.GREEN + "Shulker spawn hours set to " + hours + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid shulker hours.");
                        }
                        break;

                    case "shulkerMinutes":
                        try {
                            int minutes = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setShulkerMinutes(minutes);
                            sender.sendMessage(ChatColor.GREEN + "Shulker spawn minutes set to " + minutes + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid shulker minutes.");
                        }
                        break;

                    case "shulkerSeconds":
                        try {
                            int seconds = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setShulkerSeconds(seconds);
                            sender.sendMessage(ChatColor.GREEN + "Shulker spawn seconds set to " + seconds + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid shulker seconds.");
                        }
                        break;

                    case "locatorBarEnabled":
                        boolean locatorBarEnabled = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("enabled");
                        UHC.getPlugin().getSettings().setLocatorBarEnabled(locatorBarEnabled);
                        sender.sendMessage(ChatColor.GREEN + "Locator bar (muestra jugadores cercanos) " + (locatorBarEnabled ? "habilitado" : "deshabilitado") + ".");
                        break;

                    case "locatorBarHours":
                        try {
                            int hours = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setLocatorBarHours(hours);
                            sender.sendMessage(ChatColor.GREEN + "Locator bar activation hours set to " + hours + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid locator bar hours.");
                        }
                        break;

                    case "locatorBarMinutes":
                        try {
                            int minutes = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setLocatorBarMinutes(minutes);
                            sender.sendMessage(ChatColor.GREEN + "Locator bar activation minutes set to " + minutes + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid locator bar minutes.");
                        }
                        break;

                    case "locatorBarSeconds":
                        try {
                            int seconds = Integer.parseInt(value);
                            UHC.getPlugin().getSettings().setLocatorBarSeconds(seconds);
                            sender.sendMessage(ChatColor.GREEN + "Locator bar activation seconds set to " + seconds + ".");
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid locator bar seconds.");
                        }
                        break;

                    case "recipe":

                        if (args.length < 5) {
                            sender.sendMessage(ChatColor.RED + "Please specify a recipe name and value.");
                            return false;
                        }

                        String recipeName = args[3];
                        String recipeValue = args[4];

                        boolean enabled;
                        if (recipeValue.equals("enabled")) {
                            enabled = true;
                        } else if (recipeValue.equals("disabled")) {
                            enabled = false;
                        } else {
                            sender.sendMessage(ChatColor.RED + "Invalid recipe value. Use 'enabled' or 'disabled'.");
                            return false;
                        }

                        BaseItem itemEntry = UHC.getPlugin().getSettings().getItems().stream()
                                .filter(item -> item.getKey().getKey().equals(recipeName))
                                .findFirst().orElse(null);

                        if (itemEntry == null) {
                            sender.sendMessage(ChatColor.RED + "Unknown recipe name.");
                            return false;
                        }

                        if (enabled) {
                            itemEntry.enable();
                        } else {
                            itemEntry.disable();
                        }

                        sender.sendMessage(ChatColor.GREEN + "Recipe " + recipeName + " set to " + (enabled ? "enabled" : "disabled") + ".");
                        break;

                    default:
                        sender.sendMessage(ChatColor.RED + "Unknown setting.");
                        break;

                }

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
                break;
        }

        return true;

    }

}
