package vch.uhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;
import vch.uhc.misc.Messages;

/**
 * Handler for the /afk command. Allows players to toggle their AFK status.
 */
public class AFKCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Only players can be AFK
        if (!(sender instanceof Player player)) {
            if (sender != null) {
                sender.sendMessage(Messages.AFK_ONLY_PLAYERS());
            }
            return true;
        }

        // Toggle AFK status via AFKManager
        if (UHC.getPlugin().getAFKManager() != null) {
            UHC.getPlugin().getAFKManager().toggleAFK(player);
        }

        // Send feedback message to the player
        if (UHC.getPlugin().getAFKManager().isAFK(player.getUniqueId())) {
            player.sendMessage(Messages.AFK_NOW_AFK());
        } else {
            player.sendMessage(Messages.AFK_NO_LONGER_AFK());
        }

        return true;
    }
}
