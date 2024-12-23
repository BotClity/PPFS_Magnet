// PPFS_Magnet Plugin
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT
package com.ppfs.Commands.Magnet;

import com.ppfs.Models.Magnet;
import com.ppfs.Models.Messages;
import com.ppfs.ppfs_libs.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class SubCommandCast extends SubCommand {

    public SubCommandCast(String name) {
        super(name);
    }

    @Override
    public void noPermission(CommandSender sender, Command command, String label, String... args) {
        Messages.getInstance().getNo_permission().send(sender);
    }

    @Override
    public void handle(CommandSender sender, Command command, String label, String... args) {
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType().isAir()) {
            Messages.getInstance().getNo_item_in_right_hand().send(player);
            return;
        }

        if (Magnet.isMagnet(item)){
            Messages.getInstance().getAlready_magnet().send(player);
            return;
        }

        Magnet magnet = new Magnet(item);
        player.getInventory().setItemInMainHand(magnet.getItem());

        Messages.getInstance().getMagnet_cast_successfully().send(player);
    }

    @Override
    public List<String> complete(CommandSender sender, String... args) {
        return Collections.emptyList();
    }
}
