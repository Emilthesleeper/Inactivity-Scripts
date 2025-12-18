package com.emilsleeper.inactivityScripts;

import com.emilsleeper.inactivityScripts.commands.ReloadCommand;
import com.emilsleeper.inactivityScripts.config.ConfigManager;
import com.emilsleeper.inactivityScripts.listeners.CommandListener;
import com.emilsleeper.inactivityScripts.listeners.ServerEventListener;
import com.emilsleeper.inactivityScripts.utils.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class InactivityScriptsMain extends JavaPlugin {
    private static InactivityScriptsMain instance;
    private final ConfigManager configManager;
    private final CommandExecutor commandExecutor;

    public InactivityScriptsMain() {
        instance = this;
        this.configManager = new ConfigManager(this);
        this.commandExecutor = new CommandExecutor(this);
    }

    @Override
    public void onEnable() {
        
        ServerEventListener.register(this);
        
        // Register command listener for intercepting server commands
        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        
        getCommand("inactivityscripts").setExecutor(new ReloadCommand(this));
        getCommand("inactivityscripts").setTabCompleter((sender, command, alias, args) -> {
            if (args.length == 1) {
                return java.util.Arrays.asList("reload");
            }
            return java.util.Collections.emptyList();
        });
        
        getLogger().info("Inactivity Scripts has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Inactivity Scripts has been disabled!");
    }

    public static InactivityScriptsMain getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
