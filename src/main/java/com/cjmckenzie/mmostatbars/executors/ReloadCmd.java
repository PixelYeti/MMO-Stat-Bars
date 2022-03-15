package com.cjmckenzie.mmostatbars.executors;

import com.cjmckenzie.mmostatbars.MmoStatBars;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCmd implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String s, @NotNull String[] args) {

    sender.sendMessage("Reloading MMO Stat Bars...");

    MmoStatBars.getInstance().reloadConfig();

    sender.sendMessage(ChatColor.GREEN + "MMO Stat Bars Reloaded!");
    return false;
  }
}
