package me.lunafy.manhuntwitheffects.events;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeath implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();
        ManhuntWithEffects manhuntWithEffects = ManhuntWithEffects.getInstance();
        if(manhuntWithEffects.players.containsKey(playerUUID) && manhuntWithEffects.players.get(playerUUID) == PlayerTeam.RUNNER) {
            manhuntWithEffects.changeTeam(player, PlayerTeam.SPECTATOR);

            if(!manhuntWithEffects.validTeams()) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.kickPlayer(ChatColor.GREEN + "This game has concluded.\n" + ChatColor.RED + "All runners were killed.");
                }
            }
        }
    }
}
