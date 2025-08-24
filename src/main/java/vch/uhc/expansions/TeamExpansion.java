package vch.uhc.expansions;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import vch.uhc.UHC;
import vch.uhc.managers.PlayerManager;
import vch.uhc.misc.Settings;
import vch.uhc.models.Team;

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

        if(UHC.getPlugin().getSettings().getGameStatus() == Settings.GameStatus.IN_PROGRESS) {
            Team playerTeam = playerManager.getPlayerByUUID(player.getUniqueId()).getTeam();
            if(playerTeam != null) {
                name = ChatColor.WHITE + "[" + playerTeam.getName() + ChatColor.WHITE + "]";
            }
        }

        return name;

    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        String name = player.getName();

        if(UHC.getPlugin().getSettings().getGameStatus() == Settings.GameStatus.IN_PROGRESS) {
            Team playerTeam = playerManager.getPlayerByUUID(player.getUniqueId()).getTeam();
            if(playerTeam != null) {
                name = ChatColor.WHITE + "[" + playerTeam.getName() + ChatColor.WHITE + "]";
            }
        }

        return name;

    }

}