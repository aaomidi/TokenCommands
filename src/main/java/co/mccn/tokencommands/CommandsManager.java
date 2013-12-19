package co.mccn.tokencommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: aaomidi
 * Date: 12/19/13
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommandsManager implements CommandExecutor {
    private final TokenCommands _plugin;
    private int tokens = 0;

    public CommandsManager(TokenCommands plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("Token")) {
            switch (args.length) {
                case 0:
                    if (sender instanceof Player) {
                        if (sender.hasPermission("tokencommands.checkself")) {
                            sendMessage(sender, "&bYou currently have &a" + tokens + " &bTokens");
                            return true;
                        } else {
                            noPermissions(sender);
                            return true;
                        }
                    } else {
                        sendMessage(sender, "&cThe console does not have any tokens!");
                        return true;
                    }
                case 1:
                    if (sender.hasPermission("tokencommands.checkothers")) {
                        if (_plugin.getServer().getOfflinePlayer(args[0]) == null) {
                            sendMessage(sender, "&cThat player has never joined the server!");
                            return true;

                        } else {
                            OfflinePlayer player = _plugin.getServer().getOfflinePlayer(args[0]);
                            _plugin.tokenAPI.getTokens(player.getName());
                            sendMessage(sender, "&bThat player has &a" + tokens + " &btokens.");
                        }
                    } else {
                        noPermissions(sender);
                        return true;
                    }
                case 2:
                    if (sender.hasPermission("tokencommands.alter")) {
                        return false;
                    } else {
                        noPermissions(sender);
                    }
                case 3:
                    if (sender.hasPermission("tokencommands.alter")) {
                        if (_plugin.getServer().getOfflinePlayer(args[0]) == null) {
                            sendMessage(sender, "&cThat player has never joined the server!");
                            return true;
                        } else {
                            OfflinePlayer player = (_plugin.getServer().getOfflinePlayer(args[0]));
                            if (args[1].equalsIgnoreCase("set")) {
                                try {
                                    int newTokens = Integer.parseInt(args[2]);
                                    _plugin.tokenAPI.updateTokens(player.getName(), (_plugin.tokenAPI.getTokens(player.getName())) - newTokens);
                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                    return false;
                                }

                            } else if (args[1].equalsIgnoreCase("add")) {

                                if (args[2].charAt(0) == '+' || args[2].charAt(0) == '-') {
                                    try {
                                        char operation = args[2].charAt(0);
                                        String newArg = args[2].substring(1);
                                        int newTokens = Integer.parseInt(newArg);
                                        _plugin.tokenAPI.updateTokens(player.getName(), (operation=='+')?0+newTokens:0-newTokens);

                                        return true;

                                    } catch (NumberFormatException ex) {
                                        ex.printStackTrace();
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            }

                        }
                    }
            }
        }
        return false;
    }

    private void sendMessage(CommandSender sender, String message) {
        String prefix = "&7[&6TokenCommands&7] ";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    private void noPermissions(CommandSender sender) {
        sendMessage(sender, "&cYou do not have permissions to execute this command!");
    }

}
