// PPFS_Magnet Plugin
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT
package com.ppfs.Listeners;

import com.ppfs.Models.Magnet;
import com.ppfs.Models.Messages;
import com.ppfs.PPFS_Magnet;
import com.ppfs.Services.MagnetService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerListener implements Listener {
    private static final Logger log = LoggerFactory.getLogger(PlayerListener.class);
    private final MagnetService magnetService;

    public PlayerListener() {
        this.magnetService = MagnetService.getInstance();
        PPFS_Magnet.getInstance().getServer().getPluginManager().registerEvents(this, PPFS_Magnet.getInstance());
    }

    @EventHandler
    private void onChangeSlot(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        int previousSlot = event.getPreviousSlot();
        int nextSlot = event.getNewSlot();

        ItemStack previous = player.getInventory().getItem(previousSlot);
        ItemStack next = player.getInventory().getItem(nextSlot);

        if (magnetService.hasPlayer(player.getUniqueId())){
            if (!Magnet.isMagnet(next))magnetService.removePlayer(player.getUniqueId());
            Messages.getInstance().getMagnet_deactivated().sendActionBar(player);
            return;
        }

        if (Magnet.isMagnet(next)){
            Magnet magnet = new Magnet(next);
            magnetService.addPlayer(player.getUniqueId(), magnet.getRadius());
            Messages.getInstance().getMagnet_activated().sendActionBar(player);
            return;
        }

    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent event){

        Player player = event.getPlayer();
        if (!magnetService.hasPlayer(player.getUniqueId()))return;

        ItemStack item = event.getItemDrop().getItemStack();

        if (!Magnet.isMagnet(item))return;
        magnetService.removePlayer(player.getUniqueId());
        Messages.getInstance().getMagnet_deactivated().sendActionBar(player);
    }

    @EventHandler
    private void onSwapHand(PlayerSwapHandItemsEvent event){
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir())return;
        item = player.getInventory().getItemInOffHand();
        if (item.getType().isAir())return;


        if (!Magnet.isMagnet(item) || magnetService.hasPlayer(player.getUniqueId()))return;

        Magnet magnet = new Magnet(item);
        magnetService.addPlayer(player.getUniqueId(), magnet.getRadius());
        Messages.getInstance().getMagnet_activated().sendActionBar(player);
    }
}
