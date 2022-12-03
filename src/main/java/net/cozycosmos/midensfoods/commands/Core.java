package net.cozycosmos.midensfoods.commands;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class Core implements CommandExecutor {

    private final Main plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();

    File messages = new File(Bukkit.getServer().getPluginManager().getPlugin("MidensFoods").getDataFolder(), "messages.yml");
    FileConfiguration messagesyml = YamlConfiguration.loadConfiguration(messages);

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equals("cf")) {
        if(!sender.hasPermission("customfoods.commands.use")) {
            sender.sendMessage(messagesyml.getString("noPerm").replace("&", "§"));} else {
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
                if(sender instanceof Player) {
                    if (sender.hasPermission("customfoods.commands.give")) {
                        if (args.length > 1) {
                            if (config.getBoolean("LegacySystem") || Bukkit.getVersion().contains("1.13")) {
                                LegacyGive legGive = new LegacyGive();
                                legGive.runCommand(sender, cmd, commandLabel, args);
                                if (!legGive.itemgiven) {
                                    sender.sendMessage(ChatColor.RED + args[1] + messagesyml.getString("notRegistered").replace("&", "§"));

                                }} else {
                                Give give = new Give();
                                give.runCommand(sender, cmd, commandLabel, args);
                                if (!give.itemgiven) {
                                sender.sendMessage(ChatColor.RED + args[1] + messagesyml.getString("notRegistered").replace("&", "§"));
                            }}
                            return true;
                        } else {
                        sender.sendMessage(messagesyml.getString("pleaseEnterItem").replace("&", "§"));;
                        }
                    } else {
                        sender.sendMessage(messagesyml.getString("noPerm").replace("&", "§"));
                    }
                }

                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                    if (sender.hasPermission("customfoods.commands.reload")) {
                        Reload reload = new Reload();
                        reload.runCommand(sender, cmd, commandLabel,args);
                    } else {
                        sender.sendMessage(messagesyml.getString("noPerm").replace("&", "§"));
                    }
                } else if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
                    if (sender.hasPermission("customfoods.commands.create")) {
                        Create create = new Create();
                        create.runCommand(sender, cmd, commandLabel,args);
                    } else {
                        sender.sendMessage(messagesyml.getString("noPerm").replace("&", "§"));
                    }
                } else {sender.sendMessage(messagesyml.getString("unrecognizedCommand").replace("&", "§")); }
            } else{
                sender.sendMessage(
                 ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                    ChatColor.AQUA + "/cf: "+messagesyml.getString("CoreCommandShowHelp").replace("&", "§") +
                         ChatColor.AQUA + "/cf give: "+messagesyml.getString("CoreCommandGive").replace("&", "§") +
                         ChatColor.AQUA + "/cf reload: "+messagesyml.getString("CoreCommandReload").replace("&", "§") +
                         ChatColor.AQUA + "/cf create: "+messagesyml.getString("CoreCommandCreate").replace("&", "§") +
                         ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                );
            }
        }}
    return true;}
}
