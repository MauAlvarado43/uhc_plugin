package vch.uhc.models;

import java.util.ArrayList;

/**
 * Represents a UHC team with members and a leader.
 * Manages team composition and membership operations.
 */
public class UHCTeam { 

  private String name;
  private final ArrayList<UHCPlayer> members;
  private UHCPlayer leader;

  /**
   * Creates a new team with a name and leader.
   * 
   * @param name the team name
   * @param leader the team leader
   */
  public UHCTeam(String name, UHCPlayer leader) {
    this.name = name;
    this.leader = leader;
    this.members = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UHCPlayer getLeader() {
    return leader;
  }

  public void setLeader(UHCPlayer leader) {
    this.leader = leader;
  }

  public ArrayList<UHCPlayer> getMembers() {
    return members;
  }

  /**
   * Adds a player to the team if not already a member.
   * Checks for duplicate UUIDs before adding.
   * 
   * @param player the player to add
   */
  public void addMember(UHCPlayer player) {

    for(UHCPlayer member : members)
        if(member.getUuid().equals(player.getUuid()))
            return;

    members.add(player);

  }

  /**
   * Removes a player from the team by UUID.
   * 
   * @param player the player to remove
   */
  public void removeMember(UHCPlayer player) {
    members.removeIf(member -> member.getUuid().equals(player.getUuid()));
  }

}