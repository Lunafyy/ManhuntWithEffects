package me.lunafy.manhuntwitheffects.gameplay;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Board implements Runnable {
    private final static Board instance = new Board();
    public LocalDateTime startTime;

    private Board() {
        startTime = LocalDateTime.now();
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getScoreboard() != null && player.getScoreboard().getObjective("ManhuntFunny") != null) {
                updateScoreboard(player);
            } else {
                createNewScoreboard(player);
            }
        }
    }

    public void createNewScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("ManhuntFunny", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "CUSTOM MANHUNT");

        objective.getScore(ChatColor.DARK_PURPLE + " ").setScore(5);

        Team potionTimer = scoreboard.registerNewTeam("potionTimer");
        potionTimer.addEntry(ChatColor.DARK_PURPLE.toString());
        potionTimer.setPrefix("Next Effect: ");
        potionTimer.setSuffix(ChatColor.GREEN + "???");

        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(4);

        objective.getScore(ChatColor.WHITE + " ").setScore(3);

        Team manhuntTeam = scoreboard.registerNewTeam("manhuntTeam");
        manhuntTeam.addEntry(ChatColor.RED.toString());
        manhuntTeam.setPrefix("You are a ");
        manhuntTeam.setSuffix(ChatColor.RED + "???");

        objective.getScore(ChatColor.RED.toString()).setScore(2);

        objective.getScore(ChatColor.YELLOW + " ").setScore(1);

        Team timer = scoreboard.registerNewTeam("timer");
        timer.addEntry(ChatColor.GOLD.toString());
        timer.setPrefix("Time: ");
        timer.setSuffix(ChatColor.RED + "???");

        objective.getScore(ChatColor.GOLD.toString()).setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        if(PotionEffectRunnable.getInstance().nextEffect != null) {
            Team potionTimer = scoreboard.getTeam("potionTimer");

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextEffect = PotionEffectRunnable.getInstance().nextEffect;

            long minutes = now.until(nextEffect, ChronoUnit.MINUTES);
            now = now.plusMinutes(minutes);
            long seconds = now.until(nextEffect, ChronoUnit.SECONDS);

            if(minutes == 0) {
                potionTimer.setSuffix(ChatColor.GREEN + "" + seconds + "s");
            } else {
                potionTimer.setSuffix(ChatColor.GREEN + "" + minutes + "m " + seconds + "s");
            }


        }


        Team manhuntTeam = scoreboard.getTeam("manhuntTeam");
        String playerTeam;

        switch(ManhuntWithEffects.getInstance().players.get(player.getUniqueId())) {
            case RUNNER:
                playerTeam = ChatColor.GREEN + "RUNNER";
                break;
            case HUNTER:
                playerTeam = ChatColor.YELLOW + "HUNTER";
                break;
            default:
                playerTeam = ChatColor.GRAY + "SPECTATOR";
                break;
        }

        manhuntTeam.setSuffix(playerTeam);

        Team timer = scoreboard.getTeam("timer");

        LocalDateTime d1 = startTime;
        LocalDateTime d2 = LocalDateTime.now();

        long hours = d1.until(d2, ChronoUnit.HOURS);
        d1 = d1.plusHours(hours);
        long minutes = d1.until(d2, ChronoUnit.MINUTES);
        d1 = d1.plusMinutes(minutes);
        long seconds = d1.until(d2, ChronoUnit.SECONDS);

        timer.setSuffix(ChatColor.RED + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
    }

    public static Board getInstance() {
        return instance;
    }
}
