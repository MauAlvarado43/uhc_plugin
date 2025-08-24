package vch.uhc.managers;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import vch.uhc.models.Player;

public class PlayerManager {

    private ArrayList<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public ArrayList<Player> getPlayers() {
        return players.stream().filter(p -> p.isPlaying()).collect(Collectors.toCollection(ArrayList::new));
    }

    public Player getPlayerByUUID(UUID uuid) {

        for (Player player : players) {
            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }

        org.bukkit.entity.Player bukkitPlayer = Bukkit.getPlayer(uuid);
        Player newPlayer = new Player(bukkitPlayer.getUniqueId(), bukkitPlayer.getName());
        players.add(newPlayer);
        return newPlayer;

    }

}