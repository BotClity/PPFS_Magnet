// PPFS_Magnet Plugin
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT
package com.ppfs.Commands.Magnet;

import com.ppfs.ppfs_libs.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MagnetCommand extends AbstractCommand {
    public MagnetCommand(String name, Plugin plugin) {
        super(name, plugin);
        registerSubCommand(new SubCommandGive("give"));
        registerSubCommand(new SubCommandCast("cast"));
        registerSubCommand(new SubCommandUncast("uncast"));
        registerSubCommand(new SubCommandRadius("radius"));
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

    }


}
