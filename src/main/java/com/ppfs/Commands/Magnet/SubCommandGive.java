// PPFS_Magnet Plugin
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT
package com.ppfs.Commands.Magnet;

import com.ppfs.Models.Magnet;
import com.ppfs.Models.Messages;
import com.ppfs.Services.MagnetService;
import com.ppfs.ppfs_libs.commands.SubCommand;
import com.ppfs.ppfs_libs.models.message.Message;
import com.ppfs.ppfs_libs.models.message.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SubCommandGive extends SubCommand {
    public SubCommandGive(String name) {
        super(name);
    }

    @Override
    public void noPermission(CommandSender sender, Command command, String label, String... args) {
        Messages.getInstance().getNo_permission().send(sender);
    }

    @Override
    public void handle(CommandSender sender, Command command, String label, String... args) {
        Player player = (Player) sender;

        Player target;

        if (args.length == 0){
            target = player;
        }
        else {
            Player temp = Bukkit.getPlayer(args[0]);
            if (temp == null)target = player;
            else target = temp;
        }

        int emptySlot = target.getInventory().firstEmpty();

        if (emptySlot == -1){
            sender.sendMessage("§cУ Вас полный инвентарь");
            return;
        }

        Magnet magnet = MagnetService.getInstance().getDefaultMagnet();

        target.getInventory().addItem(magnet.getItem());
        Message msg = Messages.getInstance().getMagnet_give_successfully();
        msg.addPlaceholders(
                new Placeholders()
                        .add("player", player.getName())
                        .add("target", target.getName())
                        .add("radius", String.valueOf(magnet.getRadius()))
        );

        msg.send(player);
    }

    @Override
    public List<String> complete(CommandSender sender, String... args) {
        if (args.length == 1){
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public String getPermission(CommandSender sender, String... args) {
        return "magnet.give";
    }
}
