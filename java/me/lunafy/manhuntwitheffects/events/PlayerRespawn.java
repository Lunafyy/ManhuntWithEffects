package me.lunafy.manhuntwitheffects.events;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import me.lunafy.manhuntwitheffects.items.TrackingCompass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        ManhuntWithEffects instance = ManhuntWithEffects.getInstance();
        Player player = event.getPlayer();
        if(instance.players.containsKey(player.getUniqueId())) {
            PlayerTeam team = instance.players.get(player.getUniqueId());

            if(team == PlayerTeam.HUNTER) {
                player.getInventory().addItem(TrackingCompass.getItem());
            }
        }
    }
}
