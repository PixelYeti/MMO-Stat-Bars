package com.cjmckenzie.mmostatbars.listeners;

import com.cjmckenzie.mmostatbars.util.BarUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

  @EventHandler
  public void on(PlayerQuitEvent event) {
    BarUtils.removePlayerBossBars(event.getPlayer());
  }

  @EventHandler
  public void on(PlayerJoinEvent event) {
    // Make sure they don't have any boss bars associated with their account
    BarUtils.removePlayerBossBars(event.getPlayer());
  }

}
