package com.cjmckenzie.mmostatbars.models;

public class ExpResponse {
  private int level;
  private float currentXp;
  private float xpToNextLevel;

  public int getLevel() {
    return level;
  }

  public ExpResponse setLevel(int level) {
    this.level = level;
    return this;
  }

  public float getCurrentXp() {
    return currentXp;
  }

  public ExpResponse setCurrentXp(float currentXp) {
    this.currentXp = currentXp;
    return this;
  }

  public float getXpToNextLevel() {
    return xpToNextLevel;
  }

  public ExpResponse setXpToNextLevel(float xpToNextLevel) {
    this.xpToNextLevel = xpToNextLevel;
    return this;
  }
}
