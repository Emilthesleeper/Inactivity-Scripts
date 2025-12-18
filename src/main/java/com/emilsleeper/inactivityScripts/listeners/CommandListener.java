package com.emilsleeper.inactivityScripts.listeners;

import com.emilsleeper.inactivityScripts.InactivityScriptsMain;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandListener implements Listener {
    private final InactivityScriptsMain plugin;

    public CommandListener(InactivityScriptsMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand().toLowerCase().trim();
        if (command.equals("stop") || command.equals("restart")) {
            executeCommand("stop_command_override");
            event.setCancelled(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }.runTaskLater(plugin, plugin.getConfigManager().getConfig().getInt("delay.stop_command_override") * 20L);
        }
    }

    private void executeCommand(String commandKey) {
        plugin.getCommandExecutor().executeCommand(commandKey);
    }
}
