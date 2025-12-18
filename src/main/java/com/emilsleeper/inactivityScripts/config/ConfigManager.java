package com.emilsleeper.inactivityScripts.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConfigManager {
    private final Plugin plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        
        InputStream defaultStream = plugin.getResource("config.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + configFile + ": " + e.getMessage());
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        
        if (!configFile.exists()) {
            // Create parent directories if they don't exist
            if (!configFile.getParentFile().exists()) {
                if (!configFile.getParentFile().mkdirs()) {
                    plugin.getLogger().severe("Failed to create config directory");
                    return;
                }
            }
            
            // Save the default config if it doesn't exist
            plugin.saveResource("config.yml", false);
            plugin.getLogger().info("Created default config file");
        }
    }
    
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Component getComponent(String path) {
        String message = getConfig().getString(path, "");
        try {
            return MINI_MESSAGE.deserialize(message);
        } catch (Exception e) {
            // Fallback to legacy format if MiniMessage parsing fails
            return LegacyComponentSerializer.legacySection().deserialize(message);
        }
    }

    @Deprecated
    public String getColoredString(String path) {
        return LegacyComponentSerializer.legacySection().serialize(getComponent(path));
    }
}
