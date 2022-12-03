package net.cozycosmos.midensfoods.items;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.cozycosmos.midensfoods.api.CustomFoodEatenEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;


import net.cozycosmos.midensfoods.Main;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LegacyFoodCreator implements Listener {
	
	private final Main plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();  //Accessing the config file
	File foodValues = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "foodvalues.yml");
	FileConfiguration foodvaluesyml = YamlConfiguration.loadConfiguration(foodValues);
	File satValues = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "satvalues.yml");
	FileConfiguration satvaluesyml = YamlConfiguration.loadConfiguration(satValues);


	public void ItemRecipe() {

		config = plugin.getConfig();
		config.getConfigurationSection("Recipes").getKeys(false).forEach(recipe -> {

			//make the food

			ItemStack foodItem = new ItemStack(Material.getMaterial(config.getString("Recipes." + recipe + ".Base")));  //set the base item to the object defined as base in the config
			ItemMeta meta = foodItem.getItemMeta(); //get the item's meta


			//set the name
			meta.setDisplayName(config.getString("Recipes." + recipe + ".Name").replace("&", "§"));

			//create and set the lore
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(config.getString( "Recipes." + recipe + ".Lore").replace("&", "§"));
			meta.setLore(lore);
			foodItem.setItemMeta(meta);

			//IF the recipe is a furnace recipe
			if(config.getString("Recipes." + recipe + ".Type").equals("Furnace")) {
				@SuppressWarnings("deprecation")
				FurnaceRecipe newFurnaceRecipe = new FurnaceRecipe(foodItem, Material.getMaterial(config.getString("Recipes." + recipe + ".Recipe.Ingredient")));
				newFurnaceRecipe.setExperience(config.getInt("Recipes." + recipe + ".Recipe.Experience"));
				newFurnaceRecipe.setCookingTime(config.getInt("Recipes." + recipe + ".Recipe.Cook-Time"));
				plugin.getServer().addRecipe(newFurnaceRecipe);

			}

			//IF the "Recipes." + recipe is a crafting table recipe
			else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Shaped")) {

				NamespacedKey Key = new NamespacedKey(plugin, recipe);
				ShapedRecipe theRecipe = new ShapedRecipe(Key, foodItem);
				theRecipe.shape(config.getString("Recipes." + recipe + ".Recipe.TableLine1"),config.getString("Recipes." + recipe + ".Recipe.TableLine2"),config.getString("Recipes." + recipe + ".Recipe.TableLine3"));


				config.getConfigurationSection("Recipes." + recipe + ".Recipe.LetterKeys").getKeys(false).forEach(Rletter -> {


					theRecipe.setIngredient(Rletter.charAt(0), Material.valueOf(config.getString("Recipes." + recipe + ".Recipe.LetterKeys." + Rletter)));


				});

				plugin.getServer().addRecipe(theRecipe);
			} else if (config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("None")) {
				//Do nothing, no recipe needed! This statement is only here to prevent the error


			}  else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Shapeless")) {
				NamespacedKey Key = new NamespacedKey(plugin, recipe);
				ShapelessRecipe newShapelessRecipe = new ShapelessRecipe(Key, foodItem);
				config.getConfigurationSection("Recipes." + recipe + ".Ingredients").getKeys(false).forEach(Ingredient -> {
					if(config.getInt("Recipes." + recipe + ".Ingredients." + Ingredient) == 1) {
						newShapelessRecipe.addIngredient(Material.getMaterial(Ingredient));
					} else if (config.getInt("Recipes." + recipe + ".Ingredients." + Ingredient) > 1) {
						newShapelessRecipe.addIngredient(config.getInt("Recipes." + recipe + ".Ingredients." + Ingredient), Material.getMaterial(Ingredient));
					}
				});
				plugin.getServer().addRecipe(newShapelessRecipe);
			}

			else {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid Recipe Type At " + recipe + ". Make sure there are no typos!");
			}

		});
	}
	@EventHandler
	public void ConfigFoodItem(FoodLevelChangeEvent event) {

		config = plugin.getConfig();
		config.getConfigurationSection("Recipes").getKeys(false).forEach(food -> {
		
			//make the Chocolate Bar
			ItemStack Efood = new ItemStack(Material.getMaterial(config.getString("Recipes." + food + ".Base")));
			ItemMeta meta = Efood.getItemMeta();
			
			//set the name
			meta.setDisplayName(config.getString("Recipes." + food + ".Name").replace("&", "§"));
			
			//create and set the lore
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(config.getString( "Recipes." + food + ".Lore").replace("&", "§"));
			meta.setLore(lore);
			
			
			//set the item meta
			Efood.setItemMeta(meta);
			
			//if the entity is a player
			Player p = (Player) event.getEntity();
			if(event.getEntity() instanceof Player && p.getFoodLevel() < event.getFoodLevel()) {
				
				if(event.getEntity().getItemInHand().getItemMeta().equals(meta)) {

					List<PotionEffect> effects = new ArrayList<PotionEffect>();

					if(config.getConfigurationSection("Recipes." + food +".Effects") == null) {
						//do nothing
					} else {
						config.getConfigurationSection("Recipes." + food +".Effects").getKeys(false).forEach(effect -> {
							PotionEffect tempEff = new PotionEffect(PotionEffectType.getByName(effect),config.getInt("Recipes." + food +".Effects."+effect+".Duration"),config.getInt("Recipes." + food +".Effects."+effect+".Amplitude"));
							effects.add(tempEff);
						});
					}

					CustomFoodEatenEvent foodEvent = new CustomFoodEatenEvent(p,config.getInt("Recipes." + food + ".Id"),config.getString("Recipes." + food + ".Name"),food,config.getInt("Recipes." + food + ".Hunger-Fill"),config.getInt("Recipes." + food + ".Saturation"),Material.valueOf(config.getString("Recipes." + food + ".Base")),true,effects);
					Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(foodEvent));
					return;

				}
			
			}
		});

		}
}

	 


