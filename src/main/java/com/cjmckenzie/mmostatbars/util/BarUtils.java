package com.cjmckenzie.mmostatbars.util;

import com.cjmckenzie.mmostatbars.MmoStatBars;
import com.cjmckenzie.mmostatbars.models.BossBarTracker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BarUtils {

  private BarUtils() {
  }

  private static final Map<Player, Map<NamespacedKey, BossBarTracker>> playerBossBars = new HashMap<>();

  public static void displayBar(Player player, String professionName, float progress, int level,
      String plugin) {
    String key = String.format("mmoStatBars-%s-%s-%s", player.getUniqueId(), professionName,
        plugin);

    NamespacedKey namespacedKey = NamespacedKey.fromString(key);
    if (namespacedKey == null) {
      namespacedKey = new NamespacedKey(MmoStatBars.getInstance(), key);
    }

    Map<NamespacedKey, BossBarTracker> playersBars = playerBossBars.get(player);
    if (playersBars == null) {
      playersBars = new HashMap<>();
    }

    FileConfiguration config = MmoStatBars.getInstance().getConfig();
    String defaultFormat = config.getDefaults().getString("stat-bars.format");
    String defaultChatChar = config.getDefaults().getString("stat-bars.chatcolor-char", "&");

    String titleFormat = config.getString("stat-bars.format", defaultFormat);
    String chatChar = config.getString("stat-bars.chatcolor-char", defaultChatChar);
    String title = ChatColor.translateAlternateColorCodes(chatChar.charAt(0),
        titleFormat.replace("{profession}", StringUtils.capitalize(professionName))
            .replace("{level}", String.valueOf(level)));

    BossBarTracker bar;
    if (playersBars.containsKey(namespacedKey)) {
      bar = playersBars.get(namespacedKey);
    } else {
      BossBar bossBar = Bukkit.createBossBar(namespacedKey, title, BarColor.YELLOW, BarStyle.SOLID);
      bossBar.addPlayer(player);

      bar = new BossBarTracker()
          .setKey(namespacedKey)
          .setPlayer(player)
          .setBossBar(bossBar);
    }

    bar.setRunnable();

    bar.getBossBar().setTitle(title);
    bar.getBossBar().setProgress(progress);
    bar.getBossBar().setVisible(true);

    playersBars.put(namespacedKey, bar);

    playerBossBars.put(player, playersBars);
  }

  public static void removeBossBar(Player player, NamespacedKey key) {
    playerBossBars.get(player).remove(key);
  }

  public static List<BossBarTracker> getAllBossBars() {
    return playerBossBars.values().stream().flatMap(map -> map.values().stream())
        .collect(Collectors.toList());
  }
}
