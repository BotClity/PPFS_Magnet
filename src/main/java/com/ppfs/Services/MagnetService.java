package com.ppfs.Services;

import com.ppfs.Models.Magnet;
import com.ppfs.PPFS_Magnet;
import com.ppfs.ppfs_libs.models.menu.slots.HeadSlot;
import com.ppfs.ppfs_libs.models.message.Message;
import com.ppfs.ppfs_libs.models.message.Placeholders;
import com.ppfs.ppfs_libs.service.logger.PaperLogger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MagnetService {
    private static MagnetService instance;
    private final PPFS_Magnet plugin;
    private final double speed;
    private PaperLogger logger;
    private final Map<Item, MagnetData> magnetItems = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private Magnet defaultMagnet;

    private final Map<UUID, Integer> players = new HashMap<>();

    private static class MagnetData {
        private final UUID owner;
        private final int radius;

        public MagnetData(UUID owner, int radius) {
            this.owner = owner;
            this.radius = radius;
        }

        public UUID getOwner() {
            return owner;
        }

        public int getRadius() {
            return radius;
        }
    }

    private MagnetService() {
        plugin = PPFS_Magnet.getInstance();
        logger = PPFS_Magnet.getPaperLogger();
        speed = plugin.getConfig().getDouble("speed");

        loadDefaultItem();
        initGlobalTask();
    }

    public static MagnetService getInstance() {
        if (instance == null) {
            instance = new MagnetService();
        }
        return instance;
    }

    public void loadDefaultItem() {
        FileConfiguration cfg = plugin.getConfig();

        HeadSlot magnet = new HeadSlot();
        Material material = Material.matchMaterial(
                cfg.getString("magnet.material", "PLAYER_HEAD").toUpperCase()
        );
        if (material == null) material = Material.PLAYER_HEAD;

        magnet.setMaterial(material);
        Message displayName = new Message(cfg.getString("magnet.name", "<blue>Маг<red>нит"));
        magnet.setDisplayName(displayName);

        Message lore = new Message(cfg.getStringList("magnet.lore"));
        magnet.setLore(lore);

        if (material == Material.PLAYER_HEAD) {
            String value = cfg.getString("magnet.head_value");
            if (value != null) {
                magnet.setHeadValue(value);
            }
        }

        Placeholders placeholders = new Placeholders();
        placeholders.add("radius", String.valueOf(Magnet.getDefault_radius()));
        placeholders.add("speed", String.valueOf(speed));
        magnet.setPlaceholders(placeholders);

        defaultMagnet = new Magnet(magnet.toItemStack());
    }

    public boolean hasPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    public void addPlayer(UUID uuid, int radius) {
        players.put(uuid, radius);
    }

    public int getPlayerRadius(UUID uuid) {
        return players.getOrDefault(uuid, 0);
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
        magnetItems.entrySet().removeIf(entry -> entry.getValue().getOwner().equals(uuid));
    }

    private void initGlobalTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Integer> entry : players.entrySet()) {
                    UUID uuid = entry.getKey();
                    int radius = entry.getValue();

                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null || !player.isOnline()) {
                        removePlayer(uuid);
                        continue;
                    }

                    Location location = player.getLocation();

                    location.getWorld().getNearbyEntities(location ,radius*2, radius*2, radius*2).stream()
                            .filter(e -> e instanceof Item)
                            .map(e -> (Item) e)
                            .forEach(item -> {
                                magnetItems.merge(item, new MagnetData(uuid, radius), (existing, newData) -> {
                                    Player currentOwner = Bukkit.getPlayer(existing.getOwner());
                                    Player newOwner = Bukkit.getPlayer(newData.getOwner());
                                    if (currentOwner != null && newOwner != null) {
                                        Location currentLoc = currentOwner.getLocation();
                                        Location newLoc = newOwner.getLocation();
                                        return currentLoc.distance(item.getLocation()) > newLoc.distance(item.getLocation()) ? newData : existing;
                                    }
                                    return existing;
                                });
                            });
                }

                moveAllMagnetItems();
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private void moveAllMagnetItems() {
        for (Iterator<Map.Entry<Item, MagnetData>> it = magnetItems.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Item, MagnetData> entry = it.next();
            Item item = entry.getKey();
            MagnetData data = entry.getValue();

            Player owner = Bukkit.getPlayer(data.getOwner());
            if (owner == null || !owner.isOnline() || !item.isValid()) {
                logger.debug("Удаляем из map, т.к. owner = null/оффлайн/предмет невалиден");
                it.remove();
                continue;
            }

            Location itemLoc = item.getLocation();
            Location playerLoc = owner.getLocation().add(0, 1, 0);
            double distance = itemLoc.distance(playerLoc);

            if (distance > data.getRadius()) {
                logger.debug("Удаляем из map, т.к. distance(%s) > radius(%s)", distance, data.getRadius());
                it.remove();
                continue;
            }

            if (distance < 0.6) {
                logger.debug("Удаляем из map, т.к. distance(%s) < 0.6", distance);
                it.remove();
                continue;
            }

            if (speed <= 0) {
                logger.debug("speed = %s — предмет не двигаем!", speed);
                continue;
            }

            Vector direction = playerLoc.toVector().subtract(itemLoc.toVector()).normalize();
            item.setVelocity(direction.multiply(speed));

            logger.debug("item %s движется к %s со скоростью speed=%s", item.getItemStack().getType(), owner.getName(), speed);
        }
    }
}
