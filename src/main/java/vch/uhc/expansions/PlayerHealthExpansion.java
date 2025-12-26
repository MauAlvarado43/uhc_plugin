package vch.uhc.expansions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import vch.uhc.UHC;
import vch.uhc.misc.Settings;

public class PlayerHealthExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "uchplayerhealth";
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
        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) return "";
        Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
        if (onlinePlayer == null) return "";
        
        double health = onlinePlayer.getHealth();
        return String.format(ChatColor.RED + "❤ " + ChatColor.WHITE + "%.1f", health);
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) return "";
        if (player == null) return "";
        
        double health = player.getHealth();
        return String.format(ChatColor.RED + "❤ " + ChatColor.WHITE + "%.1f", health);
    }

}
