package com.cjmckenzie.mmostatbars.listeners;

import com.cjmckenzie.mmostatbars.util.BarUtils;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class McmmoXpGainEvent implements Listener {

  @EventHandler
  public void onXpGainEvent(McMMOPlayerXpGainEvent event) {
    Player player = event.getPlayer();

    if (event.getSkill() == null) {
      return;
    }

    float xpToNextLevel = ExperienceAPI.getXPToNextLevel(player, event.getSkill().name());
    float xpRemaining = ExperienceAPI.getXPRemaining(player, event.getSkill().name());
    xpRemaining = xpRemaining - event.getRawXpGained();

    float currentXp = xpToNextLevel - xpRemaining;

    float percentage = (currentXp / xpToNextLevel);

    int level = event.getSkillLevel();
    if (percentage > 1) {
      percentage = percentage - 1;
      level++;
    }

    BarUtils.displayBar(player, event.getSkill().name(), percentage, level, currentXp,
        xpToNextLevel, "mcmmo");
  }

}
