package me.itzzaran.stats;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class GUICommand implements CommandExecutor, Listener {


    public final Main plugin;

    public GUICommand(Main main) {
        this.plugin = main;
        Bukkit.getPluginManager().registerEvents(this, Main.instance);
    }


    public ItemStack guiitem(Material material, String title, String lore) {
        ArrayList<String> lorelist = new ArrayList<>();
        lorelist.add(lore);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(lorelist);
        item.setItemMeta(meta);
        return item;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("§6Stats van " + p.getName())) {
            e.setCancelled(true);
        }
    }


    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (s instanceof Player) {
            Player p = (Player) s;
            Playerdata playerdata = Main.instance.getPlayerData(p);
            Inventory gui = Bukkit.createInventory(p, 27, "§6Stats van " + p.getName());
            ItemStack playerkills = guiitem(Material.IRON_SWORD, "§6§uPlayers killed", "§7§l► §6" + playerdata.getPlayer_kills());
            gui.setItem(10, playerkills);
            ItemStack mobkills = guiitem(Material.BONE, "§6§uMobs killed", "§7§l► §6" + playerdata.getMob_kills());
            gui.setItem(12, mobkills);
            ItemStack timeseaten = guiitem(Material.BREAD, "§6§uTimes eaten", "§7§l► §6" + playerdata.getTimes_eaten());
            gui.setItem(14, timeseaten);
            ItemStack distancewalked = guiitem(Material.IRON_BOOTS, "§6§uDistance walked", "§7§l► §6" + playerdata.getDistance_walked());
            gui.setItem(16, distancewalked);
            p.openInventory(gui);

        }
        return false;
    }
}
