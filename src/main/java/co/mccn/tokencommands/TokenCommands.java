package co.mccn.tokencommands;

import co.mccn.tokenapi.TokenAPI;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class TokenCommands extends JavaPlugin {
    public TokenAPI tokenAPI;
    private CommandsManager commandsManager;

    @Override
    public final void onLoad() {
        if (!(new File(getDataFolder(), "config.yml").exists())) {
            saveDefaultConfig();
        }
        this.commandsManager = new CommandsManager(this);
    }

    @Override
    public final void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("TokenAPI");
        if (plugin == null) {
            getLogger().log(Level.SEVERE, "TokenAPI not found, Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            tokenAPI = (TokenAPI) plugin;

        }
        for (Command command : PluginCommandYamlParser.parse(this)) {
            this.getCommand(command.getName()).setExecutor(this.commandsManager);
        }
    }

    public final void onDisable() {
        tokenAPI.updateDatabase();

    }


}
