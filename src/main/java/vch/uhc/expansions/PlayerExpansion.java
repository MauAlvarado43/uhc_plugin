package vch.uhc.expansions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import vch.uhc.UHC;
import vch.uhc.misc.enums.GameState;
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
        vch.uhc.models.UHCPlayer p = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
        if (p == null || UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return "\u00a7f" + player.getName() + "\u00a7f";
        }
        String name = p.isAlive() ? p.getRandomName() : p.getName();
        return "\u00a7f" + name + "\u00a7f";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        vch.uhc.models.UHCPlayer p = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());

        if (params.equalsIgnoreCase("lives")) {
            if (p == null || UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
                return "";
            }
            return "\u00a7c♣ " + p.getLives();
        }

        if (p == null || UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return "\u00a7f" + player.getName() + "\u00a7f";
        }
        String name = p.isAlive() ? p.getRandomName() : p.getName();
        return "\u00a7f" + name + "\u00a7f";
    }

}
