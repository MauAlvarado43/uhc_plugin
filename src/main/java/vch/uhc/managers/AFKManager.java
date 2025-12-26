package vch.uhc.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AFKManager {

    private final Map<UUID, AFKData> afkPlayers = new HashMap<>();

    public void toggleAFK(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (isAFK(uuid)) {
            removeAFK(player);
        } else {
            setAFK(player);
        }
    }

    private void setAFK(Player player) {
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();
        
        afkPlayers.put(uuid, new AFKData(location));
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));
        player.setWalkSpeed(0.0f);
        player.setFlySpeed(0.0f);
        
        player.setAllowFlight(false);
    }

    private void removeAFK(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (!afkPlayers.containsKey(uuid)) {
            return;
        }
        
        AFKData data = afkPlayers.remove(uuid);
        
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        
        if (data.getOriginalLocation() != null) {
            player.teleport(data.getOriginalLocation());
        }
    }

    public boolean isAFK(UUID uuid) {
        return afkPlayers.containsKey(uuid);
    }

    public void clearAll() {
        afkPlayers.clear();
    }

    private static class AFKData {
        private final Location originalLocation;

        public AFKData(Location originalLocation) {
            this.originalLocation = originalLocation;
        }

        public Location getOriginalLocation() {
            return originalLocation;
        }
    }
}
