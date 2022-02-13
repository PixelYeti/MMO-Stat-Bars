package com.cjmckenzie.mmostatbars.listeners;

import com.cjmckenzie.mmostatbars.models.ExpResponse;
import com.cjmckenzie.mmostatbars.util.BarUtils;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import com.gmail.nossr50.datatypes.experience.FormulaType;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.util.player.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class McmmoXpGainEvent implements Listener {

  @EventHandler
  public void onXpGainEvent(McMMOPlayerXpGainEvent event) {
    if (event.getSkill() == null) {
      return;
    }

    Player player = event.getPlayer();
    McMMOPlayer mmoPlayer = UserManager.getPlayer(player);
    PlayerProfile playerProfile = mmoPlayer.getProfile();

    float currentXp = playerProfile.getSkillXpLevel(event.getSkill()) + event.getRawXpGained();

    ExpResponse expResponse = getNewExpInfo(mmoPlayer, event.getSkillLevel(), currentXp);

    float percentage = expResponse.getCurrentXp() / expResponse.getXpToNextLevel();

    BarUtils.displayBar(player, event.getSkill().name(), percentage, expResponse.getLevel(),
        expResponse.getCurrentXp(), expResponse.getXpToNextLevel(), "mcmmo");
  }

  private ExpResponse getNewExpInfo(McMMOPlayer player, int startLevel, float currentXp) {
    int requiredXp = getXpToNextLevel(player, startLevel);

    if (currentXp > requiredXp) {
      return getNewExpInfo(player, startLevel + 1, currentXp - requiredXp);
    }

    return new ExpResponse()
        .setLevel(startLevel)
        .setCurrentXp(currentXp)
        .setXpToNextLevel(requiredXp);
  }

  public int getXpToNextLevel(McMMOPlayer player, int startLevel) {
    int level =
        (ExperienceConfig.getInstance().getCumulativeCurveEnabled()) ? player.getPowerLevel()
            : startLevel;
    FormulaType formulaType = ExperienceConfig.getInstance().getFormulaType();

    return mcMMO.getFormulaManager().getXPtoNextLevel(level, formulaType);
  }
}
