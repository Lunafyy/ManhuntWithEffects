package me.lunafy.manhuntwitheffects.events;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.GameState;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(ManhuntWithEffects.getInstance().currentState == GameState.PRE_GAME && ManhuntWithEffects.getInstance().players.get(event.getPlayer().getUniqueId()) != PlayerTeam.SPECTATOR) {
            event.setCancelled(true);
        } else if(ManhuntWithEffects.getInstance().currentState == GameState.IN_GAME && !ManhuntWithEffects.getInstance().huntersCanMove) {
            if(ManhuntWithEffects.getInstance().players.get(event.getPlayer().getUniqueId()) == PlayerTeam.HUNTER) {
                event.setCancelled(true);
            }
        }
    }
}
