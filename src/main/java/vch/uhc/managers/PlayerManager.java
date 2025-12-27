package vch.uhc.managers;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import vch.uhc.models.UHCPlayer;

/**
 * Manages UHC player instances and provides player lookup functionality.
 */
public class PlayerManager {

    private final ArrayList<UHCPlayer> players = new ArrayList<>();

    public void addPlayer(UHCPlayer player) {
        players.add(player);
    }

    public void removePlayer(UHCPlayer player) {
        players.remove(player);
    }

    public ArrayList<UHCPlayer> getPlayers() {
        return players.stream().filter(p -> p.isPlaying()).collect(Collectors.toCollection(ArrayList::new));
    }

    public UHCPlayer getPlayerByUUID(UUID uuid) {

        for (UHCPlayer player : players) {
            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }

        Player bukkitPlayer = Bukkit.getPlayer(uuid);
        if (bukkitPlayer != null) {
            UHCPlayer newPlayer = new UHCPlayer(bukkitPlayer.getUniqueId(), bukkitPlayer.getName());
            players.add(newPlayer);
            return newPlayer;
        }
        return null;

    }

}