package me.lunafy.manhuntwitheffects.events;

import me.lunafy.manhuntwitheffects.gameplay.PotionEffectRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityType entity = event.getEntityType();

        if(entity == EntityType.ENDER_DRAGON) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.GREEN + "Runners win!", null, 20, 20, 20);
            }
        }
    }
}
