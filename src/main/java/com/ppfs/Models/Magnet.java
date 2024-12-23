// PPFS_Magnet Plugin
// Авторские права (c) 2024 PPFSS
// Лицензия: MIT
package com.ppfs.Models;

import com.ppfs.PPFS_Magnet;
import com.ppfs.ppfs_libs.nbtapi.NBT;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class Magnet {
    private ItemStack item;
    private int radius;
    @Getter
    private static final int default_radius = PPFS_Magnet.getInstance().getConfig().getInt("radius", 3);

    public Magnet(ItemStack item) {
        if (!isMagnet(item)) {
            this.item = toMagnet(item, default_radius);
            this.radius = default_radius;
            return;
        }

        this.item = item;
        this.radius = getRadius(item);

    }

    public Magnet(ItemStack item, int radius) {
        if (!isMagnet(item))
            item = toMagnet(item, radius);
        this.item = item;
        this.radius = radius;
    }

    public static boolean isMagnet(ItemStack item) {
        if (item == null) return false;

        return NBT.get(item, nbt-> nbt.hasTag("magnet") && nbt.getBoolean("magnet"));
    }

    private ItemStack toMagnet(ItemStack item, int radius) {
        NBT.modify(item, nbt -> {
            nbt.setBoolean("magnet", true);
            nbt.setInteger("radius", radius);
        });

        return item;
    }

    public ItemStack uncast() {

        NBT.modify(item, nbt->{
            nbt.removeKey("magnet");
            nbt.removeKey("radius");
        });
        return item;
    }

    private int getRadius(ItemStack item) {
        return NBT.get(item, nbt-> (Integer) nbt.getInteger("radius"));
    }

    public void updateRadius(int radius) {
        this.radius = radius;
        NBT.modify(item, nbt->{
            nbt.setInteger("radius", radius);
        });
    }
}
