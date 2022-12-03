package net.cozycosmos.midensfoods.commands;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class Create {
    private Main plugin = Main.getPlugin(Main.class);
    public Server server;

    public boolean isfood = false;
    private int hunger = 0;
    private int saturation = 0;
    private int newid = 1;

    FileConfiguration config = plugin.getConfig();  //Accessing the config file

    File foodValues = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "foodvalues.yml");
    FileConfiguration foodvaluesyml = YamlConfiguration.loadConfiguration(foodValues);
    File satValues = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "satvalues.yml");
    FileConfiguration satvaluesyml = YamlConfiguration.loadConfiguration(satValues);

    File messages = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "messages.yml");
    FileConfiguration messagesyml = YamlConfiguration.loadConfiguration(messages);

    public void runCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        config = plugin.getConfig();
        Player p = (Player) sender;
        foodvaluesyml.getConfigurationSection("").getKeys(false).forEach(food -> {
            if(p.getInventory().getItemInMainHand().getType().equals(Material.getMaterial(food))) {
                isfood = true;
                if (args.length >= 2) {
                    try {
                        hunger = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e){
                        sender.sendMessage(messagesyml.getString("InvalidArgs").replace("&", "§"));
                        return;
                    }
                    if (args.length >= 3) {
                        try {
                            saturation = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e){
                            sender.sendMessage(messagesyml.getString("InvalidArgs").replace("&", "§"));
                            return;
                        }
                        config.set("Recipes." + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace(" ","-").replace("§", "-") +".Name", p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace("§", "&"));
                        config.set("Recipes." + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace(" ","-").replace("§", "-") +".Lore", p.getInventory().getItemInMainHand().getItemMeta().getLore());
                        config.set("Recipes." + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace(" ","-").replace("§", "-") +".Base", p.getInventory().getItemInMainHand().getType().toString());
                        config.set("Recipes." + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace(" ","-").replace("§", "-") +".Hunger-Fill", hunger);
                        config.set("Recipes." + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace(" ","-").replace("§", "-") +".Saturation", saturation);
                        config.set("Recipes." + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace(" ","-").replace("§", "-") +".Type", "None");

                        config.getConfigurationSection("Recipes").getKeys(false).forEach(recipe -> {
                            newid += config.getInt("Recipes."+recipe+".Id");
                        });
                        sender.sendMessage(messagesyml.getString("FoodCreated").replace("&", "§"));
                        config.set("Recipes." + p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().replace(" ","-").replace("§", "-") +".Id", newid);
                        plugin.saveConfig();
                    }
                } else {
                    sender.sendMessage(messagesyml.getString("CreateCommandNoArgs").replace("&", "§"));
                }
            } else {

            }
        });

        if (isfood){
            //do nothing
        } else {
            sender.sendMessage(messagesyml.getString("CreateCommandNoItem").replace("&", "§"));
        }

    }
}


