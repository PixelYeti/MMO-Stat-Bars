package com.cjmckenzie.mmostatbars;

import com.cjmckenzie.mmostatbars.listeners.MMOCoreXpGainEvent;
import com.cjmckenzie.mmostatbars.listeners.McmmoXpGainEvent;
import com.cjmckenzie.mmostatbars.listeners.PlayerListener;
import com.cjmckenzie.mmostatbars.util.BarUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MmoStatBars extends JavaPlugin {

  private static MmoStatBars plugin;

  public MmoStatBars() {
    plugin = this;
  }

  public static MmoStatBars getInstance() {
    return plugin;
  }

  @Override
  public void onDisable() {
    super.onDisable();

    BarUtils.getAllBossBars().forEach(tracker -> {
      BossBar bar = tracker.getBossBar();
      bar.setVisible(false);
      bar.removeAll();

      Bukkit.removeBossBar(tracker.getKey());
    });
  }

  @Override
  public void onEnable() {

    saveDefaultConfig();

    final int configVersion =
        getConfig().contains("config-version", true) ? getConfig().getInt("config-version") : -1;
    final int defConfigVersion = getConfig().getDefaults().getInt("config-version", 1);
    if (configVersion != defConfigVersion) {
      getLogger().warning("You may be using an outdated config.yml!");
      getLogger().warning(
          "(Your config version: '{}" + configVersion + "' | Expected config version: '"
              + defConfigVersion + "')");
    }

    if (getConfig().getBoolean("stat-bars.mcmmo.enabled", true)) {
      if (getServer().getPluginManager().getPlugin("mcMMO") == null) {
        getLogger().log(Level.WARNING, "Error enabling mcMMO support. McMMO is not installed");
      } else {
        this.getServer().getPluginManager().registerEvents(new McmmoXpGainEvent(), this);
      }
    }
    if (getConfig().getBoolean("stat-bars.mmocore.enabled", true)) {
      if (getServer().getPluginManager().getPlugin("MMOCore") == null) {
        getLogger().log(Level.WARNING, "Error enabling MMOCore support. MMOCore is not installed");
      } else {
        this.getServer().getPluginManager().registerEvents(new MMOCoreXpGainEvent(), this);
      }
    }

    this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

    NamespacedKey tempKey = new NamespacedKey(this, "temp");

    List<KeyedBossBar> bossBarList = new ArrayList<>();
    Bukkit.getBossBars().forEachRemaining(bossBarList::add);

    for (KeyedBossBar keyedBossBar : bossBarList) {
      if (!keyedBossBar.getKey().getNamespace().equals(tempKey.getNamespace())) {
        continue;
      }

      keyedBossBar.removeAll();
      keyedBossBar.setVisible(false);

      Bukkit.removeBossBar(keyedBossBar.getKey());
    }
  }
}
