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

        AvailableCommands start = new AvailableCommands("start");
        AvailableCommands stop = new AvailableCommands("cancel");
        AvailableCommands pause = new AvailableCommands("pause");
        AvailableCommands info = new AvailableCommands("info");
        AvailableCommands play = new AvailableCommands("play");
        AvailableCommands leave = new AvailableCommands("leave");

        AvailableCommands settings = new AvailableCommands("settings");

        AvailableCommands set = new AvailableCommands("set");

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

        AvailableCommands recipe = new AvailableCommands("recipe");
        UHC.getPlugin().getSettings().getItems().stream().forEach(item -> {
            AvailableCommands recipeCommands = new AvailableCommands(item.getKey().getKey());
            recipeCommands.addNextArg(new AvailableCommands("enabled"));
            recipeCommands.addNextArg(new AvailableCommands("disabled"));
            recipe.addNextArg(recipeCommands);
        });

        set.addNextArg(teamMode);
        set.addNextArg(teamSize);
        set.addNextArg(playerLives);
        set.addNextArg(maxWorldSize);
        set.addNextArg(minWorldSize);
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
        set.addNextArg(recipe);

        settings.addNextArg(set);

        AvailableCommands player = new AvailableCommands("players");

        AvailableCommands playerList = new AvailableCommands("list");

        AvailableCommands setLives = new AvailableCommands("setLives");
        UHC.getPlugin().getPlayerManager().getPlayers().stream().forEach(p -> {
            UHC.getPlugin().getLogger().info(p.getName());
            AvailableCommands playerCommand = new AvailableCommands(p.getName());
            playerCommand.addNextArg(new AvailableCommands("<lives>"));
            setLives.addNextArg(playerCommand);
        });

        AvailableCommands setHealth = new AvailableCommands("setHealth");
        Bukkit.getOnlinePlayers().stream().forEach(p -> {
            AvailableCommands playerCommand = new AvailableCommands(p.getName());
            playerCommand.addNextArg(new AvailableCommands("<health>"));
            setHealth.addNextArg(playerCommand);
        });

        AvailableCommands revive = new AvailableCommands("revive");
        UHC.getPlugin().getPlayerManager().getPlayers().stream().forEach(p -> {
            revive.addNextArg(new AvailableCommands(p.getName()));
        });

        player.addNextArg(playerList);
        player.addNextArg(setLives);
        player.addNextArg(setHealth);
        player.addNextArg(revive);

        rootCommands.add(start);
        rootCommands.add(stop);
        rootCommands.add(pause);
        rootCommands.add(info);
        rootCommands.add(settings);
        rootCommands.add(player);
        rootCommands.add(play);
        rootCommands.add(leave);

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        buildRootCommands();
        return getSuggestionsRecursive(rootCommands, args, 0);
    }

    private List<String> getSuggestionsRecursive(List<AvailableCommands> commands, String[] args, int index) {

        List<String> suggestions = new ArrayList<>();

        if (index >= args.length) {
            for (AvailableCommands cmd : commands) {
                suggestions.add(cmd.getArg());
            }
        } else {
            String currentArg = args[index].toLowerCase();
            for (AvailableCommands cmd : commands) {
                if (cmd.getArg().toLowerCase().startsWith(currentArg)) {
                    if (index == args.length - 1) {
                        suggestions.add(cmd.getArg());
                    } else {
                        return getSuggestionsRecursive(cmd.getNextArgs(), args, index + 1);
                    }
                }
            }
        }

        return suggestions;

    }

}

class AvailableCommands {

    private String arg;
    private List<AvailableCommands> nextArgs;

    public AvailableCommands(String arg) {
        this.arg = arg;
        this.nextArgs = new ArrayList<>();
    }

    public String getArg() {
        return arg;
    }

    public List<AvailableCommands> getNextArgs() {
        return nextArgs;
    }

    public void addNextArg(AvailableCommands nextArg) {
        this.nextArgs.add(nextArg);
    }

}
