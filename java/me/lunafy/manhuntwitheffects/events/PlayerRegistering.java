package me.lunafy.manhuntwitheffects.events;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.GameState;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerRegistering implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ManhuntWithEffects.getInstance().registerPlayer(player);

        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        ManhuntWithEffects.getInstance().unregisterPlayer(event.getPlayer());

        if(!ManhuntWithEffects.getInstance().validTeams()) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                p.kickPlayer(ChatColor.GREEN + "This game has concluded.\n" + ChatColor.RED + "Not enough hunters (and/or) runners remaining.");
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(ManhuntWithEffects.getInstance().currentState == GameState.PRE_GAME && ManhuntWithEffects.getInstance().players.get(event.getEntity().getUniqueId()) != PlayerTeam.SPECTATOR) {
            event.setCancelled(true);
        } else if(ManhuntWithEffects.getInstance().currentState == GameState.IN_GAME && !ManhuntWithEffects.getInstance().huntersCanMove) {
            if(ManhuntWithEffects.getInstance().players.get(event.getEntity().getUniqueId()) == PlayerTeam.HUNTER) {
                event.setCancelled(true);
            }
        }
    }
}
