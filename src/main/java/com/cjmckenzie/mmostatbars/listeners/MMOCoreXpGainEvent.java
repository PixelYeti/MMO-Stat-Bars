package com.cjmckenzie.mmostatbars.listeners;

import com.cjmckenzie.mmostatbars.models.ExpResponse;
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
    int skillExp =
        playerData.getCollectionSkills().getExperience(profession) + event.getExperience();

    ExpResponse expResponse = getNewExpInfo(profession,
        playerData.getCollectionSkills().getLevel(profession), skillExp);

    float percentage = expResponse.getCurrentXp() / expResponse.getXpToNextLevel();

    BarUtils.displayBar(player, event.getProfession().getName(), percentage, expResponse.getLevel(),
        expResponse.getCurrentXp(), expResponse.getXpToNextLevel(), "mmocore");
  }

  private ExpResponse getNewExpInfo(Profession profession, int startLevel,
      float currentXp) {
    int requiredXp = getXpToNextLevel(profession, startLevel);

    if (currentXp > requiredXp) {
      return getNewExpInfo(profession, startLevel + 1, currentXp - requiredXp);
    }

    return new ExpResponse()
        .setLevel(startLevel)
        .setCurrentXp(currentXp)
        .setXpToNextLevel(requiredXp);
  }

  public int getXpToNextLevel(Profession profession, int level) {
    return profession.getExpCurve()
        .getExperience(level + 1);
  }

}
