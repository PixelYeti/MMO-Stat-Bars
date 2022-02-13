package com.cjmckenzie.mmostatbars.listeners;

import com.cjmckenzie.mmostatbars.util.BarUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    BarUtils.removePlayerBossBars(event.getPlayer());
  }

}
