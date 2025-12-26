package vch.uhc.models;

import java.util.UUID;

public class SkinAssignment {

    private final UUID playerUUID;
    private final String realPlayerName;
    private final String assignedSkin;
    private boolean revealed;

    public SkinAssignment(UUID playerUUID, String realPlayerName, String assignedSkin, boolean revealed) {
        if (playerUUID == null) {
            throw new IllegalArgumentException("Player UUID cannot be null");
        }
        if (realPlayerName == null || realPlayerName.isEmpty()) {
            throw new IllegalArgumentException("Real player name cannot be null or empty");
        }
        if (assignedSkin == null || assignedSkin.isEmpty()) {
            throw new IllegalArgumentException("Assigned skin cannot be null or empty");
        }
        if (realPlayerName.equals(assignedSkin)) {
            throw new IllegalArgumentException("Cannot assign player's own skin: " + realPlayerName);
        }

        this.playerUUID = playerUUID;
        this.realPlayerName = realPlayerName;
        this.assignedSkin = assignedSkin;
        this.revealed = revealed;
    }

    public SkinAssignment(UUID playerUUID, String realPlayerName, String assignedSkin) {
        this(playerUUID, realPlayerName, assignedSkin, false);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getRealPlayerName() {
        return realPlayerName;
    }

    public String getAssignedSkin() {
        return assignedSkin;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isValid() {
        return !realPlayerName.equals(assignedSkin);
    }

    @Override
    public String toString() {
        return "SkinAssignment{" +
                "player=" + realPlayerName +
                ", disguisedAs=" + assignedSkin +
                ", revealed=" + revealed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkinAssignment that = (SkinAssignment) o;

        return playerUUID.equals(that.playerUUID);
    }

    @Override
    public int hashCode() {
        return playerUUID.hashCode();
    }
}