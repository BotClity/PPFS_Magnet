// PPFS_Magnet Plugin 
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT

package com.ppfs.Commands.Magnet;

import com.ppfs.Models.Magnet;
import com.ppfs.Models.Messages;
import com.ppfs.ppfs_libs.commands.SubCommand;
import com.ppfs.ppfs_libs.models.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class SubCommandRadius extends SubCommand {
    public SubCommandRadius(String name) {
        super(name);
    }

    public void noPermission(CommandSender sender, Command command, String label, String... args) {
        Messages.getInstance().getNo_permission().send(sender);
    }

    @Override
    public void handle(CommandSender sender, Command command, String label, String... args) {
        Player player = (Player) sender;

        if (args.length == 0){
            Messages.getInstance().getNo_args().send(player);
            return;
        }

        int radius;

        try{
            radius = Integer.parseInt(args[0]);
        }catch (NumberFormatException exception){
            Messages.getInstance().getNot_number().send(player);
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType().isAir()) {
            Messages.getInstance().getNo_item_in_right_hand().send(player);
            return;
        }

        if (!Magnet.isMagnet(item)){
            Messages.getInstance().getItem_not_magnet().send(player);
            return;
        }

        Magnet magnet = new Magnet(item);
        magnet.updateRadius(radius);

        player.getInventory().setItemInMainHand(magnet.getItem());

        Messages.getInstance().getRadius_updated().send(player);
    }

    @Override
    public List<String> complete(CommandSender sender, String... args) {
        if (args.length == 1){
            return Collections.singletonList("Радиус");
        }
        return Collections.emptyList();
    }
}
