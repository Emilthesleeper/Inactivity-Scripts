package com.emilsleeper.inactivityScripts.utils;

import com.emilsleeper.inactivityScripts.InactivityScriptsMain;
import org.bukkit.Bukkit;

import java.io.IOException;

public class CommandExecutor {
    private final InactivityScriptsMain plugin;

    public CommandExecutor(InactivityScriptsMain plugin) {
        this.plugin = plugin;
    }

    public void executeCommand(String commandKey) {
        String command = plugin.getConfigManager().getConfig().getString("commands." + commandKey);
        if (command != null && !command.isEmpty()) {
            plugin.getLogger().info("Executing script for " + command);
            Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    String os = System.getProperty("os.name").toLowerCase();
                    Process process;
                    if (os.contains("win")) {
                        if (plugin.getConfigManager().getConfig().getBoolean("commands.use_powershell")) {
                            process = Runtime.getRuntime().exec(new String[]{"powershell.exe", "/c", command});
                        } else {
                            process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});
                        }
                    } else {
                        process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
                    }
                    if (plugin.getConfigManager().getConfig().getBoolean("breakpoints."+commandKey)) {
                        try {
                            process.waitFor();
                        } catch (InterruptedException ignore) {}
                    }
                    process.getInputStream().close();
                    process.getErrorStream().close();
                    process.getOutputStream().close();
                } catch (IOException e) {
                    plugin.getLogger().severe("Could not execute command: " + command);
                    e.printStackTrace();
                }
            });
        }
    }
}
