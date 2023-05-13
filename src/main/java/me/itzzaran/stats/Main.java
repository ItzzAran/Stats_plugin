package me.itzzaran.stats;

import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static Main instance;
    public HashMap<OfflinePlayer, Playerdata> playerdatamap = new HashMap<org.bukkit.OfflinePlayer, Playerdata>();
    Eventlistener eventlistener;
    GUICommand guiCommand;
    Holograms holograms;
    Map<Player, List<Hologram>> activeholograms = new HashMap<>();

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return;
        }
        instance = this;
        eventlistener = new Eventlistener(instance);

        guiCommand = new GUICommand(instance);
        getCommand("stats").setExecutor(guiCommand);

        holograms = new Holograms(instance);
        getCommand("statholo").setExecutor(holograms);
        Database.createTable();

        instance.saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        for(Player p : Bukkit.getOnlinePlayers()){
            Database.saveData(p);
            Main.instance.playerdatamap.remove(p);
        }
    }


    public Playerdata getPlayerData(Player p){
        if(playerdatamap.containsKey(p)){
            return playerdatamap.get(p);
        }else{
            Database.loadData(p);
            return playerdatamap.get(p);
        }
    }

    public void updatePlayerData(Playerdata dataplayer){
        OfflinePlayer p = dataplayer.getPlayer();
        playerdatamap.put(p, dataplayer);
    }

    public void createnewplayer(Player p){
        Playerdata playerdata = new Playerdata();
        playerdata.setPlayer(p);
        playerdata.setPlayer_kills(0);
        playerdata.setMob_kills(0);
        playerdata.setDistance_walked(0);
        playerdata.setTimes_eaten(0);
        Main.instance.playerdatamap.put(p, playerdata);
    }

    public void loadplayerdata(Player player, int mob_kills, int player_kills, int times_eaten, int distance_walked){
        Playerdata playerdata = new Playerdata();
        playerdata.setPlayer(player);
        playerdata.setMob_kills(mob_kills);
        playerdata.setPlayer_kills(player_kills);
        playerdata.setTimes_eaten(times_eaten);
        playerdata.setDistance_walked(distance_walked);
        Main.instance.playerdatamap.put(player, playerdata);
    }

    public Playerdata rankedplayer(String uuidstring, int mob_kills, int player_kills, int times_eaten, int distance_walked){
        UUID uuid = UUID.fromString(uuidstring);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        Playerdata playerdata = new Playerdata();
        playerdata.setPlayer(player);
        playerdata.setMob_kills(mob_kills);
        playerdata.setPlayer_kills(player_kills);
        playerdata.setTimes_eaten(times_eaten);
        playerdata.setDistance_walked(distance_walked);
        return playerdata;
    }

}
