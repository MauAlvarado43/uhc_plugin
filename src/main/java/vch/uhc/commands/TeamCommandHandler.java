package vch.uhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TeamCommandHandler {

    public static boolean onTeamCommand(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Please specify a team subcommand.");
            return false;
        }

        // create team (if settings.team is manual)
        // add member (if settings.team is manual)
        // remove member (if settings.team is manual)
        // rename team
        // leave team (if settings.team is manual)

        return true;

    }

}