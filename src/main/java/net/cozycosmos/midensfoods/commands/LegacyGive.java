package net.cozycosmos.midensfoods.commands;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;

public class LegacyGive {

	private Main plugin = Main.getPlugin(Main.class);
	public Server server;

	public boolean itemgiven = false;

	int amount;
	FileConfiguration config = plugin.getConfig();  //Accessing the config file

	File messages = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "messages.yml");
	FileConfiguration messagesyml = YamlConfiguration.loadConfiguration(messages);

	public void runCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		config = plugin.getConfig();
			config.getConfigurationSection("Recipes").getKeys(false).forEach(recipe -> {
				ItemStack foodItem = new ItemStack(Material.getMaterial(config.getString("Recipes." + recipe + ".Base")));  //set the base item to the object defined as base in the config
				ItemMeta meta = foodItem.getItemMeta(); //get the item's meta
				if (args[1].equalsIgnoreCase(recipe)) {
					if (foodItem != null) {

						//set the name
						meta.setDisplayName(config.getString("Recipes." + recipe + ".Name").replace("&", "§"));

						//create and set the lore
						ArrayList<String> lore = new ArrayList<String>();
						lore.add(config.getString( "Recipes." + recipe + ".Lore").replace("&", "§"));
						meta.setLore(lore);
						foodItem.setItemMeta(meta);

						Inventory inv = ((Player) sender).getInventory();
						sender.sendMessage(messagesyml.getString("GiveCommandGivingItem").replace("&", "§") + recipe);
						inv.addItem(foodItem);
						itemgiven = true;
						if (args.length >= 3) {
							amount = Integer.parseInt(args[2]);
							for (int i = 2; i <= amount; i++) {
								inv.addItem(foodItem);
								itemgiven = true;
							}
						}
					}
				}
			});
		}
	}







