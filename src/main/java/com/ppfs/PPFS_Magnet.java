// PPFS_Magnet Plugin
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT
package com.ppfs;

import com.ppfs.Commands.Magnet.MagnetCommand;
import com.ppfs.Listeners.PlayerListener;
import com.ppfs.Services.MagnetService;
import com.ppfs.ppfs_libs.bstats.bukkit.Metrics;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Getter
public final class PPFS_Magnet extends JavaPlugin {
    private static final Logger log = LoggerFactory.getLogger(PPFS_Magnet.class);
    private final int pluginId = 23867;
    @Getter
    private static PPFS_Magnet instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;


        File cfg = new File(getDataFolder(), "config.yml");
        if (!cfg.exists()){
            log.warn("PPFS_Magnet распространяется под лицензией MIT.");
            log.warn("Ознакомьтесь с лицензией: https://пше/LICENSE.txt");
            saveDefaultConfig();
        }

        registerMetrics();

        registerCommands();
        log.info("Команды зарегистрированы.");

        registerListeners();
        log.info("Слушатели зарегистрированы.");

        MagnetService.getInstance();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerMetrics(){
        Metrics metrics = new Metrics(instance ,pluginId);
    }

    private void registerCommands() {
        new MagnetCommand("magnet", this);
    }

    private void registerListeners(){
        new PlayerListener();
    }
}
