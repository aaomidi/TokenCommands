package co.mccn.tokencommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
                            tokens = _plugin.tokenAPI.getTokens(sender.getName());
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
                    if (args[0].equalsIgnoreCase("help")) {
                        if (sender.hasPermission("tokencommands.help")) {
                            sendMessage(sender, "&5The syntax for TokenCommands is &d/token &3[player] &e[add/set] [+/-][Value] ");
                            return true;
                        } else {
                            noPermissions(sender);
                            return true;
                        }
                    } else if (!(_plugin.getServer().getOfflinePlayer(args[0]).hasPlayedBefore())) {
                        if (sender.hasPermission("tokencommands.checkothers")) {
                            sendMessage(sender, "&cThat player has never joined the server!");
                            return true;
                        } else {
                            noPermissions(sender);
                            return true;
                        }
                    } else {
                        if (sender.hasPermission("tokencommands.checkothers")) {
                            OfflinePlayer player = _plugin.getServer().getOfflinePlayer(args[0]);
                            tokens = _plugin.tokenAPI.getTokens(player.getName());
                            sendMessage(sender, "&bThat player has &a" + tokens + " &btokens.");
                            return true;
                        } else {
                            noPermissions(sender);
                            return true;
                        }
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
                                    _plugin.tokenAPI.setTokens(player.getName(), newTokens);
                                    sendMessage(sender, "&bSet " + player.getName() + "'s tokens to&a " + newTokens);
                                    return true;
                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                    return false;
                                }

                            } else if (args[1].equalsIgnoreCase("add")) {
                                try {
                                    int newTokens = Integer.parseInt(args[2]);
                                    if (newTokens != 0) {
                                        _plugin.tokenAPI.updateTokens(player.getName(), newTokens);
                                        sendMessage(sender, ((newTokens > 0) ? "&bAdded&a " + newTokens + " &btokens to " + player.getName() : "&bSubtracted&a " + newTokens + " &b tokens from " + player.getName()));
                                        return true;
                                    }
                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                    return false;
                                }
                            } else {
                                return false;
                            }


                        }
                    }

                default:
                    return false;
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
