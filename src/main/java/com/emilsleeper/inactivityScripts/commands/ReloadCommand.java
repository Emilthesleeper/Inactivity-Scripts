package com.emilsleeper.inactivityScripts.commands;

import com.emilsleeper.inactivityScripts.InactivityScriptsMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    private final InactivityScriptsMain plugin;

    public ReloadCommand(InactivityScriptsMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("inactivityscripts.reload")) {
            sender.sendMessage(Component.text("You don't have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }

        try {
            plugin.getConfigManager().reloadConfig();
            sender.sendMessage(Component.text("Config reloaded successfully!").color(NamedTextColor.GREEN));
            return true;
        } catch (Exception e) {
            sender.sendMessage(Component.text("Error reloading config: " + e.getMessage()).color(NamedTextColor.RED));
            plugin.getLogger().severe("Error reloading config: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
}
