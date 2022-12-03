package net.cozycosmos.midensfoods.events;

import net.cozycosmos.midensfoods.Main;
import net.cozycosmos.midensfoods.api.CustomFoodEatenEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.util.List;

public class FoodEaten implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    File foodValues = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "foodvalues.yml");
    FileConfiguration foodvaluesyml = YamlConfiguration.loadConfiguration(foodValues);
    File satValues = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "satvalues.yml");
    FileConfiguration satvaluesyml = YamlConfiguration.loadConfiguration(satValues);

    @EventHandler
    public void FoodEaten (CustomFoodEatenEvent e) {
        Player p = e.getPlayer();
        List<PotionEffect> effects = e.getEffects();

        int foodlevel = p.getFoodLevel();
        float satlevel = p.getSaturation();
        foodlevel += e.getHungerFill();
        foodlevel -= foodvaluesyml.getInt(e.getFoodBase().toString());
        satlevel += e.getSaturationFill();
        satlevel -= satvaluesyml.getInt(e.getFoodBase().toString());

        p.setFoodLevel(foodlevel);
        p.setSaturation(satlevel);

        if(effects.equals(null)) {
            //do nothing
        } else {
            effects.forEach(effect -> {
                p.addPotionEffect(effect);
            });
        }


    }
}
