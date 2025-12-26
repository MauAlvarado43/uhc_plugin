package vch.uhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;

public class AFKCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Solo los jugadores pueden usar este comando.");
            return true;
        }

        Player player = (Player) sender;
        UHC.getPlugin().getAFKManager().toggleAFK(player);
        
        if (UHC.getPlugin().getAFKManager().isAFK(player.getUniqueId())) {
            player.sendMessage(ChatColor.GRAY + "Ahora estás AFK. Usa /afk nuevamente para volver.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Ya no estás AFK.");
        }
        
        return true;
    }
}
