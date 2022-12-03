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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IdFoodCreator implements Listener {

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
            meta.setCustomModelData(config.getInt("Recipes." + recipe + ".Id"));
            foodItem.setItemMeta(meta);


            //IF the recipe is a furnace recipe
            if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Furnace")) {



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

                    RecipeChoice ingr;
                    if(checkCustomIngredient(config.getString("Recipes." + recipe + ".Recipe.LetterKeys." + Rletter))) {
                        ingr = new RecipeChoice.ExactChoice(makeCustomFood(config.getString("Recipes." + recipe + ".Recipe.LetterKeys." + Rletter).substring(4)));
                    } else {
                        ingr = new RecipeChoice.MaterialChoice(Material.valueOf(config.getString("Recipes." + recipe + ".Recipe.LetterKeys." + Rletter)));
                    }

                    theRecipe.setIngredient(Rletter.charAt(0), ingr);

                });

                plugin.getServer().addRecipe(theRecipe);
            }


            else if (config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("None")) {
                //Do nothing, no recipe needed! This statement is only here to prevent the error


            } else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Smoker")) {

                NamespacedKey Key = new NamespacedKey(plugin, recipe);
                SmokingRecipe newSmokingRecipe = new SmokingRecipe(Key, foodItem, Material.getMaterial(config.getString("Recipes." + recipe + ".Recipe.Ingredient")), 0, 0);
                newSmokingRecipe.setExperience(config.getInt("Recipes." + recipe + ".Recipe.Experience"));
                newSmokingRecipe.setCookingTime(config.getInt("Recipes." + recipe + ".Recipe.Cook-Time"));
                plugin.getServer().addRecipe(newSmokingRecipe);

            } else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Campfire")) {
                NamespacedKey Key = new NamespacedKey(plugin, recipe);
                CampfireRecipe newCampRecipe = new CampfireRecipe(Key, foodItem, Material.getMaterial(config.getString("Recipes." + recipe + ".Recipe.Ingredient")), 0, 0);
                newCampRecipe.setExperience(config.getInt("Recipes." + recipe + ".Recipe.Experience"));
                newCampRecipe.setCookingTime(config.getInt("Recipes." + recipe + ".Recipe.Cook-Time"));
                plugin.getServer().addRecipe(newCampRecipe);

            } else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Shapeless")) {
                NamespacedKey Key = new NamespacedKey(plugin, recipe);
                ShapelessRecipe newShapelessRecipe = new ShapelessRecipe(Key, foodItem);
                config.getConfigurationSection("Recipes." + recipe + ".Ingredients").getKeys(false).forEach(Ingredient -> {

                    RecipeChoice ingr;
                    if(checkCustomIngredient(config.getString("Recipes." + recipe + ".Ingredients." + Ingredient))) {
                        ingr = new RecipeChoice.ExactChoice(makeCustomFood(Ingredient.substring(4)));
                    } else {
                        ingr = new RecipeChoice.MaterialChoice(Material.valueOf(Ingredient));
                    }


                    if(config.getInt("Recipes." + recipe + ".Ingredients." + Ingredient) == 1) {
                        newShapelessRecipe.addIngredient(ingr);
                    } else if (config.getInt("Recipes." + recipe + ".Ingredients." + Ingredient) > 1) {
                        for (int i = 1; i == config.getInt("Recipes." + recipe + ".Ingredients." + Ingredient); i++) {
                            newShapelessRecipe.addIngredient(ingr);
                        }
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
    public void foodEaten(FoodLevelChangeEvent event) {

        config = plugin.getConfig();

            Player p = (Player) event.getEntity();
            if(event.getEntity() instanceof Player && p.getFoodLevel() < event.getFoodLevel()) {
                if(event.getItem().hasItemMeta()){
                    if(event.getItem().getItemMeta().hasCustomModelData()){
                        config.getConfigurationSection("Recipes").getKeys(false).forEach(food -> {
                        if(event.getItem().getItemMeta().getCustomModelData() == config.getInt("Recipes." + food + ".Id")){


                            List<PotionEffect> effects = new ArrayList<PotionEffect>();

                            if(config.getConfigurationSection("Recipes." + food +".Effects") == null) {
                                //do nothing
                            } else {
                                config.getConfigurationSection("Recipes." + food +".Effects").getKeys(false).forEach(effect -> {
                                    PotionEffect tempEff = new PotionEffect(PotionEffectType.getByName(effect),config.getInt("Recipes." + food +".Effects."+effect+".Duration"),config.getInt("Recipes." + food +".Effects."+effect+".Amplitude"));
                                    effects.add(tempEff);
                                });
                            }



                            CustomFoodEatenEvent foodEvent = new CustomFoodEatenEvent(p,config.getInt("Recipes." + food + ".Id"),config.getString("Recipes." + food + ".Name"),food,config.getInt("Recipes." + food + ".Hunger-Fill"),config.getInt("Recipes." + food + ".Saturation"),Material.valueOf(config.getString("Recipes." + food + ".Base")),false,effects);
                            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(foodEvent));
                            return;



                        } else {/*do nothing*/}

        });
    }


}}}
    public boolean checkCustomIngredient(String ingr) {
        if(ingr.length()<=4) {
            return false;
        } else {
            String prefix = ingr.substring(0,4);
            if(prefix.equals("CFD-")) {
                return true;
            } else {
                return false;
            }
        }

    }

    public ItemStack makeCustomFood(String name) {
        config = plugin.getConfig();

            //make the food

            ItemStack foodItem = new ItemStack(Material.getMaterial(config.getString("Recipes." + name + ".Base")));  //set the base item to the object defined as base in the config
            ItemMeta meta = foodItem.getItemMeta(); //get the item's meta


            //set the name
            meta.setDisplayName(config.getString("Recipes." + name + ".Name").replace("&", "§"));

            //create and set the lore
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(config.getString("Recipes." + name + ".Lore").replace("&", "§"));
            meta.setLore(lore);
            meta.setCustomModelData(config.getInt("Recipes." + name + ".Id"));
            foodItem.setItemMeta(meta);
        return foodItem;
    }

}

