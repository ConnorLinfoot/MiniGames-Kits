package com.connorlinfoot.minigames.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class Main extends JavaPlugin implements Listener {
    public static Plugin instance = null;

    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        Server server = getServer();
        ConsoleCommandSender console = server.getConsoleSender();

        // Remove this when using it in your own plugin
        console.sendMessage(ChatColor.GREEN + "========== MiniGamesKits! ==========");
        console.sendMessage(ChatColor.GREEN + "=========== VERSION: 1.0 ===========");
        console.sendMessage(ChatColor.GREEN + "======== BY CONNOR LINFOOT! ========");

        Bukkit.getPluginManager().registerEvents(this,this);

        // Use this when using it in your own plugin
        //console.sendMessage(ChatColor.GREEN + "Using MiniGamesKits by Connor Linfoot!");
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

    public static ArrayList<ItemStack> getKits(){
        ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
        FileConfiguration config = instance.getConfig();
        ConfigurationSection cs = config.getConfigurationSection("Kits");
        for (String node : cs.getKeys(false)) {
            if (config.get("Kits." + node + ".Name") != null) {
                String kitName = config.getString("Kits." + node + ".Name");
                if( config.getString("Kits." + node + ".Items.1.Material") == null ) continue;
                ItemStack is = new ItemStack(Material.getMaterial(config.getString("Kits." + node + ".Items.1.Material")));
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(kitName);
                is.setItemMeta(im);
                arrayList.add(is);
            }
        }
        return arrayList;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        // This is just temp for testing
        openKitGUI(e.getPlayer());
    }

    public static void openKitGUI(final Player player){
        final Inventory inventory = Bukkit.createInventory(player,54,"Kits");
        for( ItemStack is : getKits() ){
            inventory.addItem(is);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
            @Override
            public void run() {
                player.openInventory(inventory);
            }
        }, 1L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( sender instanceof Player ){
            Player player = (Player) sender;
            openKitGUI(player);
        }
        return true;
    }
}
