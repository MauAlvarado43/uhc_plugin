package vch.uhc.expansions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import vch.uhc.UHC;
import vch.uhc.misc.enums.GameState;

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
        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return "";
        }
        Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
        if (onlinePlayer == null) {
            return "";
        }

        double health = onlinePlayer.getHealth();
        return String.format("\u00a7c❤ " + "\u00a7f" + "%.1f", health);
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return "";
        }
        if (player == null) {
            return "";
        }

        double health = player.getHealth();
        return String.format("\u00a7c❤ " + "\u00a7f" + "%.1f", health);
    }

}
