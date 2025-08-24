package vch.uhc.misc;

import org.bukkit.event.Listener;

import vch.uhc.UHC;

public class BaseListener implements Listener {

  public void register() {
    UHC.getPlugin().getServer().getPluginManager().registerEvents(this, UHC.getPlugin());
  }
  
}