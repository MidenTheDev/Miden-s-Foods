package net.cozycosmos.midensfoods.util;

import net.cozycosmos.midensfoods.Main;
import net.cozycosmos.midensfoods.api.CustomFoodEatenEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class CallCustomFoodEaten {

    private static final Main plugin = Main.getPlugin(Main.class);
    private static final FileConfiguration config = plugin.getConfig();

    public static void CallEvent(String food, Player p) {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();

        if(config.getConfigurationSection("Recipes." + food +".Effects") == null) {
            //do nothing
        } else {
            config.getConfigurationSection("Recipes." + food +".Effects").getKeys(false).forEach(effect -> {
                PotionEffect tempEff = new PotionEffect(PotionEffectType.getByName(effect),config.getInt("Recipes." + food +".Effects."+effect+".Duration"),config.getInt("Recipes." + food +".Effects."+effect+".Amplifier"));
                effects.add(tempEff);
            });
        }


        List<String> cmds = new ArrayList<String>();

        if(config.getStringList("Recipes." + food +".Commands") == null) {
            //do nothing
        } else {
            cmds = config.getStringList("Recipes." + food +".Commands");
        }


        CustomFoodEatenEvent foodEvent = new CustomFoodEatenEvent(p,config.getInt("Recipes." + food + ".Id"),config.getString("Recipes." + food + ".Name"),food,config.getInt("Recipes." + food + ".Hunger-Fill"),config.getDouble("Recipes." + food + ".Saturation"), Material.valueOf(config.getString("Recipes." + food + ".Base")),false,effects,cmds);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(foodEvent));

    }
}
