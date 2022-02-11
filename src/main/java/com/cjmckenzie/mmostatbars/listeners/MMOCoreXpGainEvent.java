package com.cjmckenzie.mmostatbars.listeners;

import com.cjmckenzie.mmostatbars.util.BarUtils;
import net.Indyuce.mmocore.api.event.PlayerExperienceGainEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.experience.Profession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MMOCoreXpGainEvent implements Listener {

  @EventHandler
  public void onMmoCoreXpGain(PlayerExperienceGainEvent event) {
    Player player = event.getPlayer();

    if (event.getProfession() == null) {
      return;
    }

    PlayerData playerData = PlayerData.get(player);
    Profession profession = event.getProfession();
    int skillExp = playerData.getCollectionSkills().getExperience(profession) + event.getExperience();
    float maxExp = profession.getExpCurve()
        .getExperience(playerData.getCollectionSkills().getLevel(profession) + 1);

    float percentage = (skillExp / maxExp);

    int level = playerData.getCollectionSkills().getLevel(profession);

    if (percentage > 1) {
      percentage = percentage - 1;
      level++;
    }
    BarUtils.displayBar(player, event.getProfession().getName(), percentage, level, skillExp, maxExp, "mmocore");
  }

}
