package me.lunafy.manhuntwitheffects;

import me.lunafy.manhuntwitheffects.commands.ChangeTeam;
import me.lunafy.manhuntwitheffects.commands.EndCredits;
import me.lunafy.manhuntwitheffects.commands.Start;
import me.lunafy.manhuntwitheffects.enums.GameState;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import me.lunafy.manhuntwitheffects.events.PlayerDeath;
import me.lunafy.manhuntwitheffects.events.PlayerMove;
import me.lunafy.manhuntwitheffects.events.PlayerRegistering;
import me.lunafy.manhuntwitheffects.gameplay.Board;
import me.lunafy.manhuntwitheffects.gameplay.Tracker;
import me.lunafy.manhuntwitheffects.items.TrackingCompass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ManhuntWithEffects extends JavaPlugin {
    private static ManhuntWithEffects instance;
    private BukkitTask boardTask;
    private BukkitTask trackingTask;

    public HashMap<UUID, PlayerTeam> players = new HashMap<UUID, PlayerTeam>();
    public GameState currentState = GameState.PRE_GAME;
    public boolean huntersCanMove = false;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("credits").setExecutor(new EndCredits());
        getCommand("changeteam").setExecutor(new ChangeTeam());
        getCommand("start").setExecutor(new Start());

        getServer().getPluginManager().registerEvents(new PlayerRegistering(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

        boardTask = getServer().getScheduler().runTaskTimer(this, Board.getInstance(), 0, 5);
        trackingTask = getServer().getScheduler().runTaskTimer(this, Tracker.getInstance(), 0, 20);
    }

    public static ManhuntWithEffects getInstance() {
        return instance;
    }

    public void registerPlayer(Player p) {
        players.put(p.getUniqueId(), PlayerTeam.SPECTATOR);
    }

    public void unregisterPlayer(Player p) {
        players.remove(p.getUniqueId());
    }

    public void changeTeam(Player p, PlayerTeam newTeam) {
        players.remove(p.getUniqueId());

        players.put(p.getUniqueId(), newTeam);

        if(newTeam == PlayerTeam.SPECTATOR) {
            p.setGameMode(GameMode.SPECTATOR);
        } else if(currentState == GameState.PRE_GAME) {
            if(!p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 10, false, false));
            }

            p.setGameMode(GameMode.SURVIVAL);

            p.teleport(getServer().getWorld("world").getSpawnLocation());
        }

        p.getInventory().clear();

        if (newTeam == PlayerTeam.HUNTER) {
            p.getInventory().addItem(TrackingCompass.getItem());
        }


    }

    public boolean validTeams() {
        boolean hasRunner = false;
        boolean hasHunter = false;

        for(Map.Entry<UUID, PlayerTeam> entry : players.entrySet()) {
            if(entry.getValue() == PlayerTeam.RUNNER && !hasRunner) {
                hasRunner = true;
            } else if (entry.getValue() == PlayerTeam.HUNTER && !hasHunter) {
                hasHunter = true;
            }

            if(hasRunner && hasHunter) return true;
        }

        return false;

    }
}
