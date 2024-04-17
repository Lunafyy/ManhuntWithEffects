package me.lunafy.manhuntwitheffects.commands;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.GameState;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import me.lunafy.manhuntwitheffects.gameplay.Board;
import me.lunafy.manhuntwitheffects.gameplay.PotionEffectRunnable;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class Start implements CommandExecutor {
    int countdown = 10; // Countdown from 10
    int task; // Task ID to cancel
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(!player.isOp()) return true;

            if(task != 0) {
                player.sendMessage(ChatColor.RED + "A game is already in progress!");
                return true;
            }

            World mainWorld = Bukkit.getWorld("world");
            mainWorld.setTime(4000);
            mainWorld.setStorm(false);
            mainWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            mainWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntWithEffects.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if(countdown != 0) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a&lGame starting in &f&l" + countdown + " &a&lseconds"));
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 1, 1);
                        }
                        countdown--;
                    } else {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a&lTHE RUNNERS HAVE BEEN RELEASED!"));
                        for(Map.Entry<UUID, PlayerTeam> set : ManhuntWithEffects.getInstance().players.entrySet()) {
                            if(set.getValue() == PlayerTeam.RUNNER) {
                                Player runner = Bukkit.getPlayer(set.getKey());
                                runner.removePotionEffect(PotionEffectType.BLINDNESS);
                                runner.playSound(runner.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                                runner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
                            }
                        }

                        Board.getInstance().startTime = LocalDateTime.now();

                        Bukkit.getScheduler().runTaskLater(ManhuntWithEffects.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                ManhuntWithEffects.getInstance().huntersCanMove = true;

                                for(Map.Entry<UUID, PlayerTeam> set : ManhuntWithEffects.getInstance().players.entrySet()) {
                                    if(set.getValue() == PlayerTeam.HUNTER) {
                                        Player hunter = Bukkit.getPlayer(set.getKey());
                                        hunter.removePotionEffect(PotionEffectType.BLINDNESS);
                                        hunter.playSound(hunter.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                                    }
                                }

                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c&lTHE HUNTERS HAVE BEEN RELEASED!"));

                                Bukkit.getScheduler().runTaskTimer(ManhuntWithEffects.getInstance(), PotionEffectRunnable.getInstance(), 0, 120 * 20);
                            }
                        }, 20 * 15);

                        ManhuntWithEffects.getInstance().currentState = GameState.IN_GAME;

                        countdown = 10;
                        Bukkit.getScheduler().cancelTask(task);
                    }
                }
            }, 20, 20); // Every 1 second this task will be run
        }
        return true;
    }
}
