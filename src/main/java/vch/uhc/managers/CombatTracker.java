package vch.uhc.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks player combat to determine if a death should be considered PvP.
 * Records the last attacker and time of attack for each player.
 */
public class CombatTracker {
    
    private static final long COMBAT_TAG_DURATION = 10000; // 10 seconds
    
    private final Map<UUID, CombatTag> combatTags = new ConcurrentHashMap<>();
    
    /**
     * Records that a player was attacked by another player.
     * 
     * @param victimUUID UUID of the player that was attacked
     * @param attackerUUID UUID of the attacking player
     */
    public void tagPlayer(UUID victimUUID, UUID attackerUUID) {
        combatTags.put(victimUUID, new CombatTag(attackerUUID, System.currentTimeMillis()));
    }
    
    /**
     * Gets the last player who attacked the victim if still in combat.
     * 
     * @param victimUUID UUID of the potential victim
     * @return UUID of the last attacker if in combat, null otherwise
     */
    public UUID getLastAttacker(UUID victimUUID) {
        CombatTag tag = combatTags.get(victimUUID);
        if (tag == null) {
            return null;
        }
        
        long timeSinceAttack = System.currentTimeMillis() - tag.timestamp;
        if (timeSinceAttack > COMBAT_TAG_DURATION) {
            combatTags.remove(victimUUID);
            return null;
        }
        
        return tag.attackerUUID;
    }
    
    /**
     * Removes combat tag from a player.
     * 
     * @param playerUUID UUID of the player
     */
    public void clearTag(UUID playerUUID) {
        combatTags.remove(playerUUID);
    }
    
    /**
     * Clears all combat tags.
     */
    public void clearAll() {
        combatTags.clear();
    }
    
    /**
     * Checks if a player is currently in combat.
     * 
     * @param playerUUID UUID of the player
     * @return true if player is in combat
     */
    public boolean isInCombat(UUID playerUUID) {
        return getLastAttacker(playerUUID) != null;
    }
    
    private static class CombatTag {
        final UUID attackerUUID;
        final long timestamp;
        
        CombatTag(UUID attackerUUID, long timestamp) {
            this.attackerUUID = attackerUUID;
            this.timestamp = timestamp;
        }
    }
}
