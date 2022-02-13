package com.cjmckenzie.mmostatbars;

import com.cjmckenzie.mmostatbars.listeners.MMOCoreXpGainEvent;
import com.cjmckenzie.mmostatbars.listeners.McmmoXpGainEvent;
import com.cjmckenzie.mmostatbars.listeners.PlayerListener;
import com.cjmckenzie.mmostatbars.util.BarUtils;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

public class MmoStatBars extends JavaPlugin {

  private static final Logger logger = LogManager.getLogManager().getLogger("Minecraft");

  private static MmoStatBars plugin;

  public MmoStatBars() {
    plugin = this;
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
      this.getServer().getPluginManager().registerEvents(new McmmoXpGainEvent(), this);
    }
    if (getConfig().getBoolean("stat-bars.mmocore.enabled", true)) {
      this.getServer().getPluginManager().registerEvents(new MMOCoreXpGainEvent(), this);
    }

    this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
  }

  public static MmoStatBars getInstance() {
    return plugin;
  }
}
