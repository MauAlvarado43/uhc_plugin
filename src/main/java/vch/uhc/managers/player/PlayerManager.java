package vch.uhc.managers.player;

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

    /**
     * Adds a new player to the manager.
     *
     * @param player The UHCPlayer instance to add.
     */
    public void addPlayer(UHCPlayer player) {
        players.add(player);
    }

    /**
     * Removes a player from the manager.
     *
     * @param player The UHCPlayer instance to remove.
     */
    public void removePlayer(UHCPlayer player) {
        players.remove(player);
    }

    /**
     * Retrieves all players who are currently actively playing (not
     * spectating/eliminated).
     *
     * @return A list of active UHCPlayers.
     */
    public ArrayList<UHCPlayer> getPlayers() {
        return players.stream().filter(p -> p.isPlaying()).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Finds a UHCPlayer by their UUID. If not found and the player is online,
     * it creates a new UHCPlayer instance.
     *
     * @param uuid The UUID of the player.
     * @return The UHCPlayer instance, or null if not found and offline.
     */
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
