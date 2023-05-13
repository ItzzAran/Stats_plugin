package me.itzzaran.stats;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class Holograms implements CommandExecutor, Listener {

    public final Main plugin;

    public Holograms(Main main) {
        this.plugin = main;
        Bukkit.getPluginManager().registerEvents(this, Main.instance);
    }


    public void deletespecificholo(String type){
        List<Location> locations = (List<Location>) Main.instance.getConfig().getList("Locations."+type+"");
        if (locations != null && !locations.isEmpty()) {
            for (Location loc : locations) {
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(!Main.instance.activeholograms.isEmpty()&&Main.instance.activeholograms.containsKey(p)) {
                        List<Hologram> holograms = Main.instance.activeholograms.get(p);
                        holograms.removeIf(hologram -> hologram.getPosition().distance(loc) < 2);
                        Main.instance.activeholograms.put(p, holograms);
                    }
                }
            }
        }
        deletallholo();
    }

    public void deletallholo(){
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(Main.instance);
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!Main.instance.activeholograms.isEmpty()&&Main.instance.activeholograms.containsKey(p)) {
                List<Hologram> holograms = Main.instance.activeholograms.get(p);
                for(Hologram hologram : holograms){
                    hologram.delete();
                }
            }
        }
        for(String type: Main.instance.getConfig().getConfigurationSection("Locations").getKeys(false)){
            List<Location> locations = (List<Location>) Main.instance.getConfig().getList("Locations."+type+"");
            if (locations != null && !locations.isEmpty()) {
                for (Hologram hologram : api.getHolograms()) {
                    for (Location loc : locations) {
                        if(hologram.getPosition().distance(loc)<=1){
                            hologram.delete();
                        }
                    }
                }
            }
        }
    }

    public void spawnallholo(Player p){
        for(String type: Main.instance.getConfig().getConfigurationSection("Locations").getKeys(false)){
            List<Location> locations = (List<Location>) Main.instance.getConfig().getList("Locations."+type+"");
            if (locations != null && !locations.isEmpty()) {
                for(Location loc : locations){
                    createholo(p, type, loc);
                }
            }
        }
    }

    public void createholo(Player p, String type, Location loc){
        for(Player play : Bukkit.getOnlinePlayers()){
            Database.saveData(play);
        }
        String dbtype = type.toLowerCase();
        String displaytype = type.replace("_", " ");
        List<Playerdata> toplist= Database.topten(dbtype);
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(Main.instance);
        Hologram hologram = api.createHologram(loc);
        VisibilitySettings visibility = hologram.getVisibilitySettings();
        visibility.setIndividualVisibility(p, VisibilitySettings.Visibility.VISIBLE);
        visibility.setGlobalVisibility(VisibilitySettings.Visibility.HIDDEN);

        hologram.getLines().appendText("§6§lMeeste "+displaytype.toLowerCase());
        hologram.getLines().appendText(" ");
        Playerdata personaldata = Main.instance.getPlayerData(p);

        if(type.equalsIgnoreCase("player_kills")) {
            int i = 1;
            for (Playerdata playerdata : toplist) {
                hologram.getLines().appendText("§e#" + i + " §a" + playerdata.getPlayer().getName() + " §7- §b" + playerdata.getPlayer_kills());
                i++;
            }
            for (int x = (10 - toplist.size()); x > 0; x--) {
                hologram.getLines().appendText("§e#" + i + " §c??? §7- §b?");
                i++;
            }
            hologram.getLines().appendText("§b");
            hologram.getLines().appendText("§7Jouw "+displaytype+": §2"+personaldata.getPlayer_kills());
            if(!Main.instance.activeholograms.isEmpty()&&Main.instance.activeholograms.containsKey(p)) {
                List<Hologram> temp = Main.instance.activeholograms.get(p);
                temp.add(hologram);
                Main.instance.activeholograms.put(p, temp);
            }
        }

        if(type.equalsIgnoreCase("mob_kills")) {
            int i = 1;
            for (Playerdata playerdata : toplist) {
                hologram.getLines().appendText("§e#" + i + " §a" + playerdata.getPlayer().getName() + " §7- §b" + playerdata.getMob_kills());
                i++;
            }
            for (int x = (10 - toplist.size()); x > 0; x--) {
                hologram.getLines().appendText("§e#" + i + " §c??? §7- §b?");
                i++;
            }
            hologram.getLines().appendText("§b");
            hologram.getLines().appendText("§7Jouw "+displaytype+": §2"+personaldata.getMob_kills());
            if(!Main.instance.activeholograms.isEmpty()&&Main.instance.activeholograms.containsKey(p)) {
                List<Hologram> temp = Main.instance.activeholograms.get(p);
                temp.add(hologram);
                Main.instance.activeholograms.put(p, temp);
            }
        }

        if(type.equalsIgnoreCase("distance_walked")) {
            int i = 1;
            for (Playerdata playerdata : toplist) {
                hologram.getLines().appendText("§e#" + i + " §a" + playerdata.getPlayer().getName() + " §7- §b" + playerdata.getDistance_walked());
                i++;
            }
            for (int x = (10 - toplist.size()); x > 0; x--) {
                hologram.getLines().appendText("§e#" + i + " §c??? §7- §b?");
                i++;
            }
            hologram.getLines().appendText("§b");
            hologram.getLines().appendText("§7Jouw "+displaytype+": §2"+personaldata.getDistance_walked());
            if(!Main.instance.activeholograms.isEmpty()&&Main.instance.activeholograms.containsKey(p)) {
                List<Hologram> temp = Main.instance.activeholograms.get(p);
                temp.add(hologram);
                Main.instance.activeholograms.put(p, temp);
            }
        }

        if(type.equalsIgnoreCase("times_eaten")) {
            int i = 1;
            for (Playerdata playerdata : toplist) {
                hologram.getLines().appendText("§e#" + i + " §a" + playerdata.getPlayer().getName() + " §7- §b" + playerdata.getTimes_eaten());
                i++;
            }
            for (int x = (10 - toplist.size()); x > 0; x--) {
                hologram.getLines().appendText("§e#" + i + " §c??? §7- §b?");
                i++;
            }
            hologram.getLines().appendText("§b");
            hologram.getLines().appendText("§7Jouw "+displaytype+": §2"+personaldata.getTimes_eaten());
            if(!Main.instance.activeholograms.isEmpty()&&Main.instance.activeholograms.containsKey(p)) {
                List<Hologram> temp = Main.instance.activeholograms.get(p);
                temp.add(hologram);
                Main.instance.activeholograms.put(p, temp);
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(Main.instance);
        if (s instanceof Player && s.hasPermission("stats.admin")) {
            Player p = (Player) s;
            if (args.length == 0) {
                p.sendMessage("§7/statholo add <Type>");
                p.sendMessage("§7/statholo remove <Type>");
                p.sendMessage("§7/statholo update");
                p.sendMessage("§7§iTypes: [player_kills, mob_kills, times_eaten, distance_walked]");
                return false;
            }
            Boolean bool = true;
            if(args.length==1 &&args[0].equalsIgnoreCase("update")) {
                bool = false;
                deletallholo();
                for (Player play : Bukkit.getOnlinePlayers()) {
                    spawnallholo(play);
                }
                p.sendMessage("§7Alle stat holo's zijn geupdate!");
            }
            String type = "Player_kills";
            if (args[0].equalsIgnoreCase("remove")&& args.length==2 && args[1].equalsIgnoreCase(type)) {
                bool = false;
                deletespecificholo(type);
                p.sendMessage("§7Holo's verwijdert");
                List<Location> emptylist = new ArrayList<>();
                Main.instance.getConfig().set("Locations."+type+"", emptylist);
                this.plugin.saveConfig();
                for(Player play : Bukkit.getOnlinePlayers()){
                    spawnallholo(play);
                }
            }
            if (args[0].equalsIgnoreCase("add") && args.length==2 && args[1].equalsIgnoreCase(type)) {
                bool = false;
                List<Location> locations = (List<Location>) Main.instance.getConfig().getList("Locations."+type+"");
                Location loc = p.getLocation().add(0, 3, 0);
                if (locations != null && !locations.isEmpty()) {
                    locations.add(loc);
                }else{
                    locations = new ArrayList<>();
                    locations.add(loc);
                }
                Main.instance.getConfig().set("Locations."+type+"", locations);
                this.plugin.saveConfig();
                createholo(p, type, loc);
                p.sendMessage("§7List of type "+type+" created!");
            }

            String type1 = "Mob_kills";
            if (args[0].equalsIgnoreCase("remove") && args.length==2 && args[1].equalsIgnoreCase(type1)) {
                bool = false;
                deletespecificholo(type1);
                p.sendMessage("§7Holo's verwijdert");
                List<Location> emptylist = new ArrayList<>();
                Main.instance.getConfig().set("Locations."+type1+"", emptylist);
                this.plugin.saveConfig();
                for(Player play : Bukkit.getOnlinePlayers()){
                    spawnallholo(play);
                }
            }
            if (args[0].equalsIgnoreCase("add") && args.length==2 && args[1].equalsIgnoreCase(type1)) {
                bool = false;
                List<Location> locations = (List<Location>) Main.instance.getConfig().getList("Locations."+type1+"");
                Location loc = p.getLocation().add(0, 3, 0);
                if (locations != null && !locations.isEmpty()) {
                    locations.add(loc);
                }else{
                    locations = new ArrayList<>();
                    locations.add(loc);
                }
                Main.instance.getConfig().set("Locations."+type1+"", locations);
                this.plugin.saveConfig();
                createholo(p, type1, loc);
                p.sendMessage("§7List of type "+type1+" created!");
            }

            String type2 = "Times_eaten";
            if (args[0].equalsIgnoreCase("remove") && args.length==2 && args[1].equalsIgnoreCase(type2)) {
                bool = false;
                deletespecificholo(type2);
                p.sendMessage("§7Holo's verwijdert");
                List<Location> emptylist = new ArrayList<>();
                Main.instance.getConfig().set("Locations."+type2+"", emptylist);
                this.plugin.saveConfig();
                for(Player play : Bukkit.getOnlinePlayers()){
                    spawnallholo(play);
                }
            }
            if (args[0].equalsIgnoreCase("add") && args.length==2 && args[1].equalsIgnoreCase(type2)) {
                bool = false;
                List<Location> locations = (List<Location>) Main.instance.getConfig().getList("Locations."+type2+"");
                Location loc = p.getLocation().add(0, 3, 0);
                if (locations != null && !locations.isEmpty()) {
                    locations.add(loc);
                }else{
                    locations = new ArrayList<>();
                    locations.add(loc);
                }
                Main.instance.getConfig().set("Locations."+type2+"", locations);
                this.plugin.saveConfig();
                createholo(p, type2, loc);
                p.sendMessage("§7List of type "+type2+" created!");
            }

            String type3 = "Distance_walked";
            if (args[0].equalsIgnoreCase("remove") && args.length==2 && args[1].equalsIgnoreCase(type3)) {
                bool = false;
                deletespecificholo(type3);
                p.sendMessage("§7Holo's verwijdert");
                List<Location> emptylist = new ArrayList<>();
                Main.instance.getConfig().set("Locations."+type3+"", emptylist);
                this.plugin.saveConfig();
                for(Player play : Bukkit.getOnlinePlayers()){
                    spawnallholo(play);
                }
            }
            if (args[0].equalsIgnoreCase("add") && args.length==2 && args[1].equalsIgnoreCase(type3)) {
                bool = false;
                List<Location> locations = (List<Location>) Main.instance.getConfig().getList("Locations."+type3+"");
                Location loc = p.getLocation().add(0, 3, 0);
                if (locations != null && !locations.isEmpty()) {
                    locations.add(loc);
                }else{
                    locations = new ArrayList<>();
                    locations.add(loc);
                }
                Main.instance.getConfig().set("Locations."+type3+"", locations);
                this.plugin.saveConfig();
                createholo(p, type3, loc);
                p.sendMessage("§7List of type "+type3+" created!");
            }
            if(bool){
                p.performCommand("statholo");
            }
            return false;
//        Location where = p.getLocation();
//        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(Main.instance);
//        Hologram hologram = api.createHologram(where);
//        api.getHolograms().forEach();
//        TextHologramLine textLine = hologram.getLines().appendText("A hologram line");
        } else {
            s.sendMessage("Jij hebt niet de rechten om dit command te gebruiken");
        }
        return false;
    }


}
