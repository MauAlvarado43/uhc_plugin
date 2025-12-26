package vch.uhc.expansions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import vch.uhc.UHC;
import vch.uhc.misc.Settings;

public class PlayerLivesExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "uchplayerlives";
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
        if (p == null || UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS)  return "";
        return ChatColor.GREEN + "♣ " + p.getLives();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        vch.uhc.models.Player p = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
        if (p == null || UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS)  return "";
        return ChatColor.GREEN + "♣ " + p.getLives();
    }

}