package com.emilsleeper.inactivityScripts.listeners;

import com.emilsleeper.inactivityScripts.InactivityScriptsMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerEventListener implements Listener {
    private final InactivityScriptsMain plugin;

    public ServerEventListener(InactivityScriptsMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerStart(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
            executeCommand("server_start");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Bukkit.getOnlinePlayers().size() == 1) {
            executeCommand("player_count_1");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Bukkit.getOnlinePlayers().size() == 1) {
            executeCommand("player_count_0");
        }
    }

    private void executeCommand(String commandKey) {
        plugin.getCommandExecutor().executeCommand(commandKey);
    }

    public static void register(InactivityScriptsMain plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ServerEventListener(plugin), plugin);
    }
}