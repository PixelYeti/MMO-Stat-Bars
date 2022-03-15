package com.cjmckenzie.mmostatbars.util;

import com.cjmckenzie.mmostatbars.MmoStatBars;
import com.cjmckenzie.mmostatbars.models.BossBarTracker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
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
                                float exp, float maxExp, int gainedExp, String plugin) {
    String key = String.format("mmoStatBars-%s-%s-%s", player.getUniqueId(),
        professionName.replace(" ", "_").toLowerCase(),
        plugin);

    NamespacedKey namespacedKey = NamespacedKey.fromString(key);
    if (namespacedKey == null) {
      namespacedKey = new NamespacedKey(MmoStatBars.getInstance(), key);
    }

    Map<NamespacedKey, BossBarTracker> playersBars = playerBossBars.get(player);
    if (playersBars == null) {
      playersBars = new HashMap<>();
    }

    String title = createTitle(professionName, level, progress, exp, maxExp, gainedExp);

    BossBarTracker bar;
    if (playersBars.containsKey(namespacedKey)) {
      bar = playersBars.get(namespacedKey);
    } else {
      BossBar bossBar = Bukkit.createBossBar(namespacedKey, title, getBarColor(professionName),
          BarStyle.SEGMENTED_10);
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
    if (!playerBossBars.containsKey(player)) {
      return;
    }

    if (!playerBossBars.get(player).containsKey(key)) {
      return;
    }
    playerBossBars.get(player).remove(key);
  }

  public static List<BossBarTracker> getAllBossBars() {
    return playerBossBars.values().stream().flatMap(map -> map.values().stream())
        .collect(Collectors.toList());
  }

  private static String createTitle(String professionName, int level, float progress,
                                    float currentXp, float levelXp, int gainedExp) {
    if (level == 0) {
      return "Learning a new skill...";
    }

    FileConfiguration config = MmoStatBars.getInstance().getConfig();
    String defaultFormat = config.getDefaults().getString("stat-bars.format", "");
    String defaultChatChar = config.getDefaults().getString("stat-bars.chatcolor-char", "&");

    String titleFormat = config.getString("stat-bars.format", defaultFormat);
    String chatChar = config.getString("stat-bars.chatcolor-char", defaultChatChar);

    DecimalFormat df = new DecimalFormat("#.#");
    df.setRoundingMode(RoundingMode.HALF_EVEN);

    return ChatColor.translateAlternateColorCodes(chatChar.charAt(0),
        titleFormat.replace("{profession}",
                Arrays.stream(professionName.toLowerCase().split(" "))
                    .map(StringUtils::capitalize)
                    .collect(Collectors.joining(" ")))
            .replace("{level}", String.valueOf(level))
            .replace("{percentage}", df.format(progress * 100) + "%")
            .replace("{currentXp}", String.valueOf(Math.round(currentXp)))
            .replace("{levelTotalXp}", String.valueOf(Math.round(levelXp)))
            .replace("{gainedXp}", String.valueOf(gainedExp))
    );
  }

  private static BarColor getBarColor(String profession) {
    FileConfiguration config = MmoStatBars.getInstance().getConfig();

    String defaultColor = config.getString("stat-bars.default-bar-color", "YELLOW");

    String professionColor = config.getString("stat-bars.professions." + profession.toLowerCase(),
        defaultColor);

    try {
      return BarColor.valueOf(professionColor);
    } catch (IllegalArgumentException | NullPointerException e) {
      MmoStatBars.getInstance().getLogger()
          .warning("Invalid color '" + professionColor + "' for '" + profession + "'");

      return BarColor.valueOf(defaultColor);
    }
  }

  public static void removePlayerBossBars(Player player) {
    if (!playerBossBars.containsKey(player)) {
      return;
    }

    Map<NamespacedKey, BossBarTracker> playersBars = playerBossBars.get(player);
    for (BossBarTracker bossBarTracker : playersBars.values()) {
      bossBarTracker.getBossBar().removeAll();
      bossBarTracker.getBossBar().setVisible(false);

      Bukkit.removeBossBar(bossBarTracker.getKey());
    }

    playerBossBars.remove(player);
  }
}
