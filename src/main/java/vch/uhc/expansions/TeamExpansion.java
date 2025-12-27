package vch.uhc.expansions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import vch.uhc.UHC;
import vch.uhc.managers.PlayerManager;
import vch.uhc.misc.enums.GameState;
import vch.uhc.models.UHCTeam;

public class TeamExpansion extends PlaceholderExpansion {

    private final PlayerManager playerManager;

    public TeamExpansion() {
        this.playerManager = UHC.getPlugin().getPlayerManager();
    }

    @Override
    public String getIdentifier() {
        return "uchteam";
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

        String name = "";

        if (UHC.getPlugin().getSettings().getGameState() == GameState.IN_PROGRESS) {
            UHCTeam playerTeam = playerManager.getPlayerByUUID(player.getUniqueId()).getTeam();
            if (playerTeam != null) {
                name = "\u00a7f[" + playerTeam.getName() + "\u00a7f]";
            }
        }

        return name;

    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        String name = player.getName();

        if (UHC.getPlugin().getSettings().getGameState() == GameState.IN_PROGRESS) {
            UHCTeam playerTeam = playerManager.getPlayerByUUID(player.getUniqueId()).getTeam();
            if (playerTeam != null) {
                name = "\u00a7f[" + playerTeam.getName() + "\u00a7f]";
            }
        }

        return name;

    }

}
