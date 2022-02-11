package com.cjmckenzie.mmostatbars.models;

import com.cjmckenzie.mmostatbars.MmoStatBars;
import com.cjmckenzie.mmostatbars.util.BarUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarTracker {

  private NamespacedKey key;
  private Player player;
  private BossBar bossBar;
  private BukkitRunnable runnable;

  public NamespacedKey getKey() {
    return key;
  }

  public BossBarTracker setKey(NamespacedKey key) {
    this.key = key;
    return this;
  }

  public Player getPlayer() {
    return player;
  }

  public BossBarTracker setPlayer(Player player) {
    this.player = player;
    return this;
  }

  public BossBar getBossBar() {
    return bossBar;
  }

  public BossBarTracker setBossBar(BossBar bossBar) {
    this.bossBar = bossBar;
    return this;
  }

  public BossBarTracker setRunnable() {
    if (runnable != null) {
      runnable.cancel();
    }

    runnable = new BukkitRunnable() {

      @Override
      public void run() {
        bossBar.removeAll();
        bossBar.setVisible(false);
        Bukkit.removeBossBar(key);

        BarUtils.removeBossBar(player, key);
      }
    };

    runnable.runTaskLater(MmoStatBars.getInstance(), 100);

    return this;
  }
}
