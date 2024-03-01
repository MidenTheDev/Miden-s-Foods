package net.cozycosmos.midensfoods.events;

import net.cozycosmos.midensfoods.Main;
import net.cozycosmos.midensfoods.api.CustomFoodEatenEvent;
import net.cozycosmos.midensfoods.util.VanillaFood;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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


    @EventHandler
    public void FoodEaten (CustomFoodEatenEvent e) {
        Player p = e.getPlayer();
        List<PotionEffect> effects = e.getEffects();
        List<String> commands = e.getCommands();

        int foodlevel= p.getFoodLevel();
        float satlevel= p.getSaturation();

         e.getOriginalEvent().setCancelled(true);
         foodlevel += e.getHungerFill();
         satlevel += e.getSaturationFill();




        if (foodlevel < 0) {
            foodlevel = 0;
        }
        if (satlevel < 0) {
            satlevel = 0;
        }
            p.setFoodLevel(foodlevel);
            p.setSaturation(satlevel);

            if(effects.equals(null)) {
            //do nothing
            } else {
                effects.forEach(effect -> {
                    p.addPotionEffect(effect);
                });
            }

            if (commands.equals(null)) {
                //do nothing
            } else {
                commands.forEach(cmd -> {
                    String finalCmd = cmd.replace("%player%", e.getPlayer().getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                });

            }


    }
}
