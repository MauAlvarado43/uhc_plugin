package vch.uhc.models;

import java.util.ArrayList;

public class Team { 

  private String name;
  private final ArrayList<Player> members;
  private Player leader;

  public Team(String name, Player leader) {
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

  public Player getLeader() {
    return leader;
  }

  public void setLeader(Player leader) {
    this.leader = leader;
  }

  public ArrayList<Player> getMembers() {
    return members;
  }

  public void addMember(Player player) {

    for(Player member : members)
        if(member.getUuid().equals(player.getUuid()))
            return;

    members.add(player);

  }

  public void removeMember(Player player) {
    members.removeIf(member -> member.getUuid().equals(player.getUuid()));
  }

}