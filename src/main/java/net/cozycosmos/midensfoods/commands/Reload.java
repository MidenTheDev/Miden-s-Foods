package net.cozycosmos.midensfoods.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import org.bukkit.command.CommandSender;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


public class Reload{
	private Main plugin = Main.getPlugin(Main.class);



	File messages = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "messages.yml");
	FileConfiguration messagesyml = YamlConfiguration.loadConfiguration(messages);
	
	public void runCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
			sender.sendMessage(messagesyml.getString("ReloadCommandReloading").replace("&", "§"));
			plugin.getServer().getConsoleSender().sendMessage(messagesyml.getString("ReloadCommandReloading").replace("&", "§"));
			sender.sendMessage(messagesyml.getString("ReloadCommandLag").replace("&", "§"));
			reload();
			plugin.getServer().getConsoleSender().sendMessage(messagesyml.getString("ReloadCommandFinished").replace("&", "§"));
			sender.sendMessage(messagesyml.getString("ReloadCommandFinished").replace("&", "§"));


	}
	public void reload() {
		plugin.unloadRecipes();
		plugin.unloadEvents();
		plugin.reloadConfig();
		messagesyml = YamlConfiguration.loadConfiguration(messages);
		plugin.registerRecipes();
		plugin.registerEvents();
	}
}