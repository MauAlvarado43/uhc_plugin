package vch.uhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TeamCommandHandler {

    public static boolean onTeamCommand(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Please specify a team subcommand.");
            return false;
        }


        return true;

    }

}