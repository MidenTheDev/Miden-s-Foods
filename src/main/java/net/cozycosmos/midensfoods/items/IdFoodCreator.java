package net.cozycosmos.midensfoods.items;



import net.cozycosmos.midensfoods.util.CallCustomFoodEaten;
import net.cozycosmos.midensfoods.util.GenerateFoodItemstack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.*;

import net.cozycosmos.midensfoods.Main;

public class IdFoodCreator implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();  //Accessing the config file


    public void ItemRecipe() {
        config = plugin.getConfig();
        config.getConfigurationSection("Recipes").getKeys(false).forEach(recipe -> {

            //make the food

            ItemStack foodItem = GenerateFoodItemstack.withID(recipe);
            foodItem.setAmount(config.getInt("Recipes." + recipe + ".Recipe.Amount",1));

            //IF the recipe is a furnace recipe
            if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Furnace")) {
                NamespacedKey Key = new NamespacedKey(plugin, recipe);

                RecipeChoice ingr;
                if(checkCustomIngredient(config.getString("Recipes." + recipe + ".Recipe.Ingredient"))) {
                    ingr = new RecipeChoice.ExactChoice(GenerateFoodItemstack.withID(config.getString("Recipes." + recipe + ".Recipe.Ingredient").substring(4)));
                } else {
                    ingr = new RecipeChoice.MaterialChoice(Material.valueOf(config.getString("Recipes." + recipe + ".Recipe.Ingredient")));
                }

                FurnaceRecipe newFurnaceRecipe = new FurnaceRecipe(Key, foodItem, ingr, 0, 0);
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
                        ingr = new RecipeChoice.ExactChoice(GenerateFoodItemstack.withID(config.getString("Recipes." + recipe + ".Recipe.LetterKeys." + Rletter).substring(4)));
                    } else {
                        ingr = new RecipeChoice.MaterialChoice(Material.valueOf(config.getString("Recipes." + recipe + ".Recipe.LetterKeys." + Rletter)));
                    }

                    theRecipe.setIngredient(Rletter.charAt(0), ingr);

                });

                plugin.getServer().addRecipe(theRecipe);
            }


            else if (config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("None")) {
                //Do nothing, no recipe needed! This statement is only here to prevent the error
            }

            else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Smoker")) {

                NamespacedKey Key = new NamespacedKey(plugin, recipe);

                RecipeChoice ingr;
                if(checkCustomIngredient(config.getString("Recipes." + recipe + ".Recipe.Ingredient"))) {
                    ingr = new RecipeChoice.ExactChoice(GenerateFoodItemstack.withID(config.getString("Recipes." + recipe + ".Recipe.Ingredient").substring(4)));
                } else {
                    ingr = new RecipeChoice.MaterialChoice(Material.valueOf(config.getString("Recipes." + recipe + ".Recipe.Ingredient")));
                }

                SmokingRecipe newSmokingRecipe = new SmokingRecipe(Key, foodItem, ingr, 0, 0);
                newSmokingRecipe.setExperience(config.getInt("Recipes." + recipe + ".Recipe.Experience"));
                newSmokingRecipe.setCookingTime(config.getInt("Recipes." + recipe + ".Recipe.Cook-Time"));
                plugin.getServer().addRecipe(newSmokingRecipe);

            }

            else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Campfire")) {
                NamespacedKey Key = new NamespacedKey(plugin, recipe);
                CampfireRecipe newCampRecipe;

                RecipeChoice ingr;
                if(checkCustomIngredient(config.getString("Recipes." + recipe + ".Recipe.Ingredient"))) {
                    ingr = new RecipeChoice.ExactChoice(GenerateFoodItemstack.withID(config.getString("Recipes." + recipe + ".Recipe.Ingredient").substring(4)));
                } else {
                    ingr = new RecipeChoice.MaterialChoice(Material.valueOf(config.getString("Recipes." + recipe + ".Recipe.Ingredient")));
                }
                newCampRecipe = new CampfireRecipe(Key, foodItem, ingr, 0, 0);
                newCampRecipe.setExperience(config.getInt("Recipes." + recipe + ".Recipe.Experience"));
                newCampRecipe.setCookingTime(config.getInt("Recipes." + recipe + ".Recipe.Cook-Time"));
                plugin.getServer().addRecipe(newCampRecipe);

            }

            else if(config.getString("Recipes." + recipe + ".Type").equalsIgnoreCase("Shapeless")) {
                NamespacedKey Key = new NamespacedKey(plugin, recipe);
                ShapelessRecipe newShapelessRecipe = new ShapelessRecipe(Key, foodItem);
                config.getConfigurationSection("Recipes." + recipe + ".Ingredients").getKeys(false).forEach(Ingredient -> {
                    RecipeChoice ingr;
                    if(checkCustomIngredient(Ingredient)) {
                        ingr = new RecipeChoice.ExactChoice(GenerateFoodItemstack.withID(Ingredient.substring(4)));
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


                            CallCustomFoodEaten.CallEvent(food,p);
                            return;



                        } else {/*do nothing*/}

        });
    }


}}}
    public boolean checkCustomIngredient(String ingr) {
        if(ingr.length()<=4) {
            return false;
        } else {
            if(ingr.substring(0,4).equals("CFD-")) {
                return true;
            } else {
                return false;
            }
        }

    }



}

