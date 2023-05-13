package me.itzzaran.stats;

import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class Eventlistener implements Listener {

    public final Main plugin;

    public Eventlistener(Main main) {
        this.plugin = main;
        Bukkit.getPluginManager().registerEvents(this, Main.instance);

    }


    @EventHandler
    public void playerkills(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        if (victim.getKiller() != null) {
            Player p = victim.getKiller();
            Playerdata playerdata = Main.instance.getPlayerData(p);
            int current_kills = playerdata.getPlayer_kills();
            playerdata.setPlayer_kills(current_kills + 1);
            Main.instance.updatePlayerData(playerdata);
        }
    }

    @EventHandler
    public void mobkills(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) e.getEntity();
                if (entity.getHealth() <= e.getFinalDamage()) {
                    Playerdata playerdata = Main.instance.getPlayerData(p);
                    int current_mobkills = playerdata.getMob_kills();
                    playerdata.setMob_kills(current_mobkills + 1);
                    Main.instance.updatePlayerData(playerdata);
                }
            }
        }
    }

    @EventHandler
    public void eat(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        Playerdata playerdata = Main.instance.getPlayerData(p);
        int current_eaten = playerdata.getTimes_eaten();
        playerdata.setTimes_eaten(current_eaten + 1);
        Main.instance.updatePlayerData(playerdata);
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location old = e.getFrom();
        Location nieuw = e.getTo();
        if (old.getBlockX() != nieuw.getBlockX() | old.getBlockZ() != nieuw.getBlockZ()) {
            Playerdata playerdata = Main.instance.getPlayerData(p);
            int distance_walked = playerdata.getDistance_walked();
            playerdata.setDistance_walked(distance_walked + 1);
            Main.instance.updatePlayerData(playerdata);

        }
    }


    @EventHandler
    public void leave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Database.saveData(p);
        if(!Main.instance.activeholograms.isEmpty()&&Main.instance.activeholograms.containsKey(p)) {
            List<Hologram> holograms = Main.instance.activeholograms.get(p);
            for (Hologram hologram : holograms) {
                hologram.delete();
            }
            Main.instance.activeholograms.remove(p);
        }
        Main.instance.playerdatamap.remove(p);
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Database.loadData(p);
        Main.instance.holograms.spawnallholo(p);
    }

}
