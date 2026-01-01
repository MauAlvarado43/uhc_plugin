package vch.uhc.misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import vch.uhc.UHC;

public class CommandCompleter implements TabCompleter {

    private List<AvailableCommands> rootCommands;

    public CommandCompleter() {
        buildRootCommands();
    }

    private void buildRootCommands() {

        rootCommands = new ArrayList<>();

        AvailableCommands start = new AvailableCommands("start", vch.uhc.misc.enums.Permission.START.getNode());
        AvailableCommands stop = new AvailableCommands("cancel", vch.uhc.misc.enums.Permission.CANCEL.getNode());
        AvailableCommands reload = new AvailableCommands("reload", vch.uhc.misc.enums.Permission.RELOAD.getNode());
        AvailableCommands pause = new AvailableCommands("pause", vch.uhc.misc.enums.Permission.PAUSE.getNode());
        AvailableCommands info = new AvailableCommands("info", vch.uhc.misc.enums.Permission.INFO.getNode());
        AvailableCommands join = new AvailableCommands("join", vch.uhc.misc.enums.Permission.JOIN.getNode());
        AvailableCommands leave = new AvailableCommands("leave", vch.uhc.misc.enums.Permission.LEAVE.getNode());

        AvailableCommands settings = new AvailableCommands("settings", vch.uhc.misc.enums.Permission.SETTINGS.getNode());

        AvailableCommands set = new AvailableCommands("set", vch.uhc.misc.enums.Permission.SETTINGS_SET.getNode());

        AvailableCommands gameMode = new AvailableCommands("gameMode");
        gameMode.addNextArg(new AvailableCommands("pvd"));
        gameMode.addNextArg(new AvailableCommands("pvp"));
        gameMode.addNextArg(new AvailableCommands("resourceRush"));

        AvailableCommands teamMode = new AvailableCommands("teamMode");
        teamMode.addNextArg(new AvailableCommands("auto"));
        teamMode.addNextArg(new AvailableCommands("manual"));
        teamMode.addNextArg(new AvailableCommands("inGame"));

        AvailableCommands teamSize = new AvailableCommands("teamSize");
        teamSize.addNextArg(new AvailableCommands("<size>"));

        AvailableCommands playerLives = new AvailableCommands("playerLives");
        playerLives.addNextArg(new AvailableCommands("<lives>"));

        AvailableCommands maxWorldSize = new AvailableCommands("maxWorldSize");
        maxWorldSize.addNextArg(new AvailableCommands("<size>"));

        AvailableCommands minWorldSize = new AvailableCommands("minWorldSize");
        minWorldSize.addNextArg(new AvailableCommands("<size>"));

        AvailableCommands gameHours = new AvailableCommands("gameHours");
        gameHours.addNextArg(new AvailableCommands("<hours>"));

        AvailableCommands gameMinutes = new AvailableCommands("gameMinutes");
        gameMinutes.addNextArg(new AvailableCommands("<minutes>"));

        AvailableCommands gameSeconds = new AvailableCommands("gameSeconds");
        gameSeconds.addNextArg(new AvailableCommands("<seconds>"));

        AvailableCommands agreementHours = new AvailableCommands("agreementHours");
        agreementHours.addNextArg(new AvailableCommands("<hours>"));

        AvailableCommands agreementMinutes = new AvailableCommands("agreementMinutes");
        agreementMinutes.addNextArg(new AvailableCommands("<minutes>"));

        AvailableCommands agreementSeconds = new AvailableCommands("agreementSeconds");
        agreementSeconds.addNextArg(new AvailableCommands("<seconds>"));

        AvailableCommands minWorldBorderHours = new AvailableCommands("minWorldBorderHours");
        minWorldBorderHours.addNextArg(new AvailableCommands("<hours>"));

        AvailableCommands minWorldBorderMinutes = new AvailableCommands("minWorldBorderMinutes");
        minWorldBorderMinutes.addNextArg(new AvailableCommands("<minutes>"));

        AvailableCommands minWorldBorderSeconds = new AvailableCommands("minWorldBorderSeconds");
        minWorldBorderSeconds.addNextArg(new AvailableCommands("<seconds>"));

        AvailableCommands maxTeamInGameHours = new AvailableCommands("maxTeamInGameHours");
        maxTeamInGameHours.addNextArg(new AvailableCommands("<hours>"));

        AvailableCommands maxTeamInGameMinutes = new AvailableCommands("maxTeamInGameMinutes");
        maxTeamInGameMinutes.addNextArg(new AvailableCommands("<minutes>"));

        AvailableCommands maxTeamInGameSeconds = new AvailableCommands("maxTeamInGameSeconds");
        maxTeamInGameSeconds.addNextArg(new AvailableCommands("<seconds>"));

        AvailableCommands endPortalHours = new AvailableCommands("endPortalHours");
        endPortalHours.addNextArg(new AvailableCommands("<hours>"));

        AvailableCommands endPortalMinutes = new AvailableCommands("endPortalMinutes");
        endPortalMinutes.addNextArg(new AvailableCommands("<minutes>"));

        AvailableCommands endPortalSeconds = new AvailableCommands("endPortalSeconds");
        endPortalSeconds.addNextArg(new AvailableCommands("<seconds>"));

        AvailableCommands shulkerEnabled = new AvailableCommands("shulkerEnabled");
        shulkerEnabled.addNextArg(new AvailableCommands("true"));
        shulkerEnabled.addNextArg(new AvailableCommands("false"));
        shulkerEnabled.addNextArg(new AvailableCommands("enabled"));
        shulkerEnabled.addNextArg(new AvailableCommands("disabled"));

        AvailableCommands shulkerHours = new AvailableCommands("shulkerHours");
        shulkerHours.addNextArg(new AvailableCommands("<hours>"));

        AvailableCommands shulkerMinutes = new AvailableCommands("shulkerMinutes");
        shulkerMinutes.addNextArg(new AvailableCommands("<minutes>"));

        AvailableCommands shulkerSeconds = new AvailableCommands("shulkerSeconds");
        shulkerSeconds.addNextArg(new AvailableCommands("<seconds>"));

        AvailableCommands locatorBarEnabled = new AvailableCommands("locatorBarEnabled");
        locatorBarEnabled.addNextArg(new AvailableCommands("true"));
        locatorBarEnabled.addNextArg(new AvailableCommands("false"));
        locatorBarEnabled.addNextArg(new AvailableCommands("enabled"));
        locatorBarEnabled.addNextArg(new AvailableCommands("disabled"));

        AvailableCommands locatorBarHours = new AvailableCommands("locatorBarHours");
        locatorBarHours.addNextArg(new AvailableCommands("<hours>"));

        AvailableCommands locatorBarMinutes = new AvailableCommands("locatorBarMinutes");
        locatorBarMinutes.addNextArg(new AvailableCommands("<minutes>"));

        AvailableCommands locatorBarSeconds = new AvailableCommands("locatorBarSeconds");
        locatorBarSeconds.addNextArg(new AvailableCommands("<seconds>"));

        AvailableCommands recipe = new AvailableCommands("recipe");
        UHC.getPlugin().getSettings().getItems().stream().forEach(item -> {
            AvailableCommands recipeCommands = new AvailableCommands(item.getKey().getKey());
            recipeCommands.addNextArg(new AvailableCommands("enabled"));
            recipeCommands.addNextArg(new AvailableCommands("disabled"));
            recipe.addNextArg(recipeCommands);
        });

        AvailableCommands brand = new AvailableCommands("brand");
        brand.addNextArg(new AvailableCommands("<name>"));

        set.addNextArg(gameMode);
        set.addNextArg(teamMode);
        set.addNextArg(teamSize);
        set.addNextArg(playerLives);
        set.addNextArg(maxWorldSize);
        set.addNextArg(minWorldSize);
        set.addNextArg(brand);
        set.addNextArg(gameHours);
        set.addNextArg(gameMinutes);
        set.addNextArg(gameSeconds);
        set.addNextArg(agreementHours);
        set.addNextArg(agreementMinutes);
        set.addNextArg(agreementSeconds);
        set.addNextArg(minWorldBorderHours);
        set.addNextArg(minWorldBorderMinutes);
        set.addNextArg(minWorldBorderSeconds);
        set.addNextArg(maxTeamInGameHours);
        set.addNextArg(maxTeamInGameMinutes);
        set.addNextArg(maxTeamInGameSeconds);
        set.addNextArg(endPortalHours);
        set.addNextArg(endPortalMinutes);
        set.addNextArg(endPortalSeconds);
        set.addNextArg(shulkerEnabled);
        set.addNextArg(shulkerHours);
        set.addNextArg(shulkerMinutes);
        set.addNextArg(shulkerSeconds);
        set.addNextArg(locatorBarEnabled);
        set.addNextArg(locatorBarHours);
        set.addNextArg(locatorBarMinutes);
        set.addNextArg(locatorBarSeconds);
        set.addNextArg(recipe);

        settings.addNextArg(set);

        AvailableCommands player = new AvailableCommands("players", vch.uhc.misc.enums.Permission.PLAYERS.getNode());

        AvailableCommands playerList = new AvailableCommands("list", vch.uhc.misc.enums.Permission.PLAYERS_LIST.getNode());

        AvailableCommands setLives = new AvailableCommands("setLives", vch.uhc.misc.enums.Permission.PLAYERS_SETLIVES.getNode());
        UHC.getPlugin().getPlayerManager().getPlayers().stream().forEach(p -> {
            AvailableCommands playerCommand = new AvailableCommands(p.getName());
            playerCommand.addNextArg(new AvailableCommands("<lives>"));
            setLives.addNextArg(playerCommand);
        });

        AvailableCommands setHealth = new AvailableCommands("setHealth", vch.uhc.misc.enums.Permission.PLAYERS_SETHEALTH.getNode());
        Bukkit.getOnlinePlayers().stream().forEach(p -> {
            AvailableCommands playerCommand = new AvailableCommands(p.getName());
            playerCommand.addNextArg(new AvailableCommands("<health>"));
            setHealth.addNextArg(playerCommand);
        });

        AvailableCommands revive = new AvailableCommands("revive", vch.uhc.misc.enums.Permission.PLAYERS_REVIVE.getNode());
        UHC.getPlugin().getPlayerManager().getPlayers().stream().forEach(p -> {
            revive.addNextArg(new AvailableCommands(p.getName()));
        });

        player.addNextArg(playerList);
        player.addNextArg(setLives);
        player.addNextArg(setHealth);
        player.addNextArg(revive);

        AvailableCommands team = new AvailableCommands("team");
        
        AvailableCommands teamCreate = new AvailableCommands("create", vch.uhc.misc.enums.Permission.TEAM_CREATE.getNode());
        teamCreate.addNextArg(new AvailableCommands("<nombre>"));
        
        AvailableCommands teamAdd = new AvailableCommands("add", vch.uhc.misc.enums.Permission.TEAM_ADD.getNode());
        Bukkit.getOnlinePlayers().stream().forEach(p -> {
            AvailableCommands playerCommand = new AvailableCommands(p.getName());
            UHC.getPlugin().getTeamManager().getTeams().stream().forEach(t -> {
                playerCommand.addNextArg(new AvailableCommands(t.getName()));
            });
            teamAdd.addNextArg(playerCommand);
        });
        
        AvailableCommands teamRemove = new AvailableCommands("remove", vch.uhc.misc.enums.Permission.TEAM_REMOVE.getNode());
        Bukkit.getOnlinePlayers().stream().forEach(p -> {
            AvailableCommands playerCommand = new AvailableCommands(p.getName());
            UHC.getPlugin().getTeamManager().getTeams().stream().forEach(t -> {
                playerCommand.addNextArg(new AvailableCommands(t.getName()));
            });
            teamRemove.addNextArg(playerCommand);
        });
        
        AvailableCommands teamRename = new AvailableCommands("rename", vch.uhc.misc.enums.Permission.TEAM_RENAME.getNode());
        UHC.getPlugin().getTeamManager().getTeams().stream().forEach(t -> {
            AvailableCommands teamCommand = new AvailableCommands(t.getName());
            teamCommand.addNextArg(new AvailableCommands("<nuevoNombre>"));
            teamRename.addNextArg(teamCommand);
        });
        
        AvailableCommands teamLeave = new AvailableCommands("leave", vch.uhc.misc.enums.Permission.TEAM_LEAVE.getNode());
        
        AvailableCommands teamList = new AvailableCommands("list", vch.uhc.misc.enums.Permission.TEAM_LIST.getNode());
        
        team.addNextArg(teamCreate);
        team.addNextArg(teamAdd);
        team.addNextArg(teamRemove);
        team.addNextArg(teamRename);
        team.addNextArg(teamLeave);
        team.addNextArg(teamList);

        AvailableCommands menu = new AvailableCommands("menu", vch.uhc.misc.enums.Permission.MENU.getNode());
        AvailableCommands stats = new AvailableCommands("stats", vch.uhc.misc.enums.Permission.STATS.getNode());
        AvailableCommands afk = new AvailableCommands("afk", vch.uhc.misc.enums.Permission.AFK.getNode());
        
        AvailableCommands backup = new AvailableCommands("backup", vch.uhc.misc.enums.Permission.ADMIN.getNode());
        backup.addNextArg(new AvailableCommands("save"));
        backup.addNextArg(new AvailableCommands("load"));
        backup.addNextArg(new AvailableCommands("clear"));

        rootCommands.add(start);
        rootCommands.add(stop);
        rootCommands.add(reload);
        rootCommands.add(pause);
        rootCommands.add(info);
        rootCommands.add(settings);
        rootCommands.add(player);
        rootCommands.add(team);
        rootCommands.add(join);
        rootCommands.add(leave);
        rootCommands.add(menu);
        rootCommands.add(stats);
        rootCommands.add(afk);
        rootCommands.add(backup);

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        buildRootCommands();
        return getSuggestionsRecursive(sender, rootCommands, args, 0);
    }

    private List<String> getSuggestionsRecursive(CommandSender sender, List<AvailableCommands> commands, String[] args, int index) {

        List<String> suggestions = new ArrayList<>();

        if (index >= args.length) {
            for (AvailableCommands cmd : commands) {
                if (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission())) {
                    suggestions.add(cmd.getArg());
                }
            }
        } else {
            String currentArg = args[index].toLowerCase();
            for (AvailableCommands cmd : commands) {
                if (cmd.getArg().toLowerCase().startsWith(currentArg)) {
                    if (cmd.getPermission() != null && !sender.hasPermission(cmd.getPermission())) {
                        continue;
                    }
                    if (index == args.length - 1) {
                        suggestions.add(cmd.getArg());
                    } else {
                        return getSuggestionsRecursive(sender, cmd.getNextArgs(), args, index + 1);
                    }
                }
            }
        }

        return suggestions;

    }

}

class AvailableCommands {

    private final String arg;
    private final List<AvailableCommands> nextArgs;
    private String permission;

    public AvailableCommands(String arg) {
        this.arg = arg;
        this.nextArgs = new ArrayList<>();
    }

    public AvailableCommands(String arg, String permission) {
        this.arg = arg;
        this.nextArgs = new ArrayList<>();
        this.permission = permission;
    }

    public String getArg() {
        return arg;
    }

    public List<AvailableCommands> getNextArgs() {
        return nextArgs;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void addNextArg(AvailableCommands nextArg) {
        this.nextArgs.add(nextArg);
    }

}
