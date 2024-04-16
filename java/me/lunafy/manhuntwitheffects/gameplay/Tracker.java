package me.lunafy.manhuntwitheffects.gameplay;

import jline.internal.Nullable;
import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.GameState;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Tracker implements Runnable {
    private final static Tracker instance = new Tracker();

    public static Tracker getInstance() { return instance; }

    @Override
    public void run() {
        if(ManhuntWithEffects.getInstance().currentState != GameState.IN_GAME) return;

        for(Map.Entry<UUID, PlayerTeam> set : ManhuntWithEffects.getInstance().players.entrySet()) {
            if(set.getValue() == PlayerTeam.HUNTER) {
                //String distance = "";

                Player hunter = Bukkit.getPlayer(set.getKey());
                Player nearestRunner = getNearestRunner(hunter);

                /*
                if(nearestRunner == null) {
                    distance = "???";
                } else {
                    distance = ((int)hunter.getLocation().distance(nearestRunner.getLocation())) + "m";
                }*/

                if(nearestRunner != null) {
                    hunter.setCompassTarget(nearestRunner.getLocation());
                }



                //hunter.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Nearest Runner: " + distance));
            }
        }
    }

    public static @Nullable Player getNearestRunner(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        ArrayList<Player> playersInWorld = new ArrayList<Player>();

        for(Map.Entry<UUID, PlayerTeam> set : ManhuntWithEffects.getInstance().players.entrySet()) {
            Player plr = Bukkit.getPlayer(set.getKey());
            if(set.getValue() == PlayerTeam.RUNNER && plr.getWorld() == player.getWorld()) {
                playersInWorld.add(plr);
            }
        }

        if(playersInWorld.size()==0) return null;
        playersInWorld.remove(player);
        playersInWorld.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(location)));
        return playersInWorld.get(0);
    }
}
