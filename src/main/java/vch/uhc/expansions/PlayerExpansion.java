package vch.uhc.expansions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import vch.uhc.UHC;
import vch.uhc.misc.Settings;

public class PlayerExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "uchplayer";
    }

    @Override
    public String getAuthor() {
        return "VCH";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        vch.uhc.models.Player p = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
        if (p == null || UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) return ChatColor.WHITE + player.getName() + ChatColor.WHITE;
        String name = p.isAlive() ? p.getRandomName() : p.getName();
        return ChatColor.WHITE + name + ChatColor.WHITE;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        vch.uhc.models.Player p = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
        
        if (params.equalsIgnoreCase("lives")) {
            if (p == null || UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS)
                return "";
            return ChatColor.RED + "♣ " + p.getLives();
        }
        
        if (p == null || UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) 
            return ChatColor.WHITE + player.getName() + ChatColor.WHITE;
        String name = p.isAlive() ? p.getRandomName() : p.getName();
        return ChatColor.WHITE + name + ChatColor.WHITE;
    }

}