package net.cozycosmos.midensfoods.util;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GenerateFoodItemstack {

    private static final Main plugin = Main.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    public static ItemStack noID(String name) {
        //make the Fooditem
        ItemStack Efood = new ItemStack(Material.getMaterial(config.getString("Recipes." + name + ".Base")));
        ItemMeta meta = Efood.getItemMeta();

        //set the name
        meta.setDisplayName(config.getString("Recipes." + name + ".Name").replace("&", "§"));

        //create and set the lore
        List<String> lore = new ArrayList<String>();
        List<String> configLore = config.getStringList("Recipes."+name+".Lore");
        configLore.forEach(entry ->{
            lore.add(entry.replace("&", "§"));
        });
        meta.setLore(lore);


        //set the item meta
        Efood.setItemMeta(meta);
        return Efood;
    }

    public static ItemStack withID(String name) {
        //make the food

        ItemStack foodItem = noID(name);
        ItemMeta meta = foodItem.getItemMeta(); //get the item's meta

        meta.setCustomModelData(config.getInt("Recipes." + name + ".Id"));
        foodItem.setItemMeta(meta);
        return foodItem;
    }



}
