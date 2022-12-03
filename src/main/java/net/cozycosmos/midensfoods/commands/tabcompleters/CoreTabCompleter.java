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
        baseCommands.add("reload"); baseCommands.add("give"); baseCommands.add("create");
        config = plugin.getConfig();
        config.getConfigurationSection("Recipes").getKeys(false).forEach(recipe -> {
            foodlist.add(recipe);
        });
        if(sender instanceof Player){
        if(cmd.getName().equalsIgnoreCase("cf")) {
            if (args.length==1) {
                return baseCommands;
            } else {

                return foodlist;
            }
        }
        }
        return null;
    }
}
