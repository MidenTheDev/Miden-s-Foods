package net.cozycosmos.midensfoods;

import net.cozycosmos.midensfoods.commands.tabcompleters.CoreTabCompleter;
import net.cozycosmos.midensfoods.commands.Core;
import net.cozycosmos.midensfoods.events.FoodEaten;
import net.cozycosmos.midensfoods.extras.Metrics;
import net.cozycosmos.midensfoods.extras.UpdateChecker;
import net.cozycosmos.midensfoods.items.IdFoodCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.cozycosmos.midensfoods.items.LegacyFoodCreator;

import java.io.File;


public class Main extends JavaPlugin{


	public JavaPlugin instance;
	public PluginManager pm;
	public ConsoleCommandSender cs;

	File foodvaluesYml = new File(getDataFolder()+"/foodvalues.yml");
	File satvaluesYml = new File(getDataFolder()+"/satvalues.yml");
	File messagesYml = new File(getDataFolder()+"/messages.yml");


	public void onEnable() {


		instance = this;

		pm = Bukkit.getServer().getPluginManager();
		cs = Bukkit.getServer().getConsoleSender();


		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
			cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN+ "If this is your first time using the plugin, you may have to reload the plugin to get it to work properly! (/cf reload)");
		}
		int pluginId = 12329; // <-- Replace with the id of your plugin!
		Metrics metrics = new Metrics(this, pluginId);



		new UpdateChecker(this, 88653).getVersion(version -> {
			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
				cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN + "You're running the latest version!");
			} else {
				cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN + "There's a new update available!");
				cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN + "You're running version "+this.getDescription().getVersion()+ " While the latest version is "+version+"!");
			}
		});

		if(!foodvaluesYml.exists()){
			this.saveResource("foodvalues.yml", false);
		}else{
			// do nothing
		}
		if(!satvaluesYml.exists()){
			this.saveResource("satvalues.yml", false);
		}else{
			// do nothing
		}
		if(!messagesYml.exists()){
			this.saveResource("messages.yml", false);
		}else{
			// do nothing
		}


		ItemStack recipeItemstack = new ItemStack(Material.REDSTONE);


		getConfig().options().copyDefaults();
		saveDefaultConfig();
		registerRecipes();
		registerEvents();
		registerCommands();

		cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN + "Miden's Foods Enabled");
		cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN + "This plugin is still in Beta! Expect Bugs!");



	}

	public void onDisable() {
		unloadRecipes();
		cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.RED + "Miden's Foods Disabled");

	}

	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();

		if(getConfig().getBoolean("LegacySystem") || Bukkit.getVersion().contains("1.13")) {
			pm.registerEvents(new LegacyFoodCreator(), this);
			cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN + "Legacy food system enabled!");
			cs.sendMessage(ChatColor.GRAY +"[MidensFoods] "+ChatColor.GREEN + "The modern ID system is incompatible with versions under 1.14!");
		}else {
			pm.registerEvents(new IdFoodCreator(), this);
		}
		pm.registerEvents(new FoodEaten(), this);
	}
	public void registerRecipes() {
		if(getConfig().getBoolean("LegacySystem") || Bukkit.getVersion().contains("1.13")){

			LegacyFoodCreator recipe = new LegacyFoodCreator();
			recipe.ItemRecipe();
		} else {
			IdFoodCreator recipe = new IdFoodCreator();
			recipe.ItemRecipe();
		}




	}

	public void registerCommands() {
		getCommand("cf").setExecutor(new Core());
		getCommand("cf").setTabCompleter(new CoreTabCompleter());
	}
	public void unloadRecipes() {
		Bukkit.resetRecipes();

	}
	public void unloadEvents() {
		HandlerList.unregisterAll(new LegacyFoodCreator());
		HandlerList.unregisterAll(new IdFoodCreator());


	}



}

