package me.lunafy.manhuntwitheffects.gameplay;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PotionEffectRunnable implements Runnable {
    private final static PotionEffectRunnable instance = new PotionEffectRunnable();
    public LocalDateTime lastEffect;
    public LocalDateTime nextEffect;

    @Override
    public void run() {
        effectGiven();

        PotionEffectType[] potionEffectTypes = PotionEffectType.values();
        Random random = new Random();

        for(Map.Entry<UUID, PlayerTeam> entry : ManhuntWithEffects.getInstance().players.entrySet()) {
            if(entry.getValue() != PlayerTeam.SPECTATOR) {
                Player player = Bukkit.getPlayer(entry.getKey());
                PotionEffect effect = new PotionEffect(potionEffectTypes[random.nextInt(potionEffectTypes.length)], 15 * 20, random.nextInt(5));

                player.addPotionEffect(effect);

                player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 0.5f, 1f);
            }
        }
    }

    public static PotionEffectRunnable getInstance() { return instance; }

    public void effectGiven() {
        lastEffect = LocalDateTime.now();
        nextEffect = LocalDateTime.now().plusMinutes(2);
    }
}
