package net.cozycosmos.midensfoods.commands.tabcompleters;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CoreTabCompleter implements TabCompleter {
    public List<String> baseCommands = new ArrayList<String>();
    public List<String> foodlist = new ArrayList<String>();
    private Main plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();



    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        config = plugin.getConfig();
        foodlist.clear();
        if(sender instanceof Player){
        if(cmd.getName().equalsIgnoreCase("cf")) {

            if (args.length==1) {
                baseCommands.add("reload"); baseCommands.add("give"); baseCommands.add("create");

                return baseCommands;
            } else if (args.length==2 && args[0].equalsIgnoreCase("give")){
                config.getConfigurationSection("Recipes").getKeys(false).forEach(recipe -> {
                    if (args[1].length()<=0) {
                        foodlist.add(recipe);
                    } else if (recipe.toLowerCase().contains(args[1].toLowerCase())) {
                        foodlist.add(recipe);
                    }

                });
                return foodlist;
            }
        }
        }
        return null;
    }
}
