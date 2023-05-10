package one.crafters.playerdeathcoords.events;

import one.crafters.playerdeathcoords.DiscordWebhook;
import one.crafters.playerdeathcoords.PlayerDeathCoords;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;

public class DeathsAndRespawns implements Listener {


    private static final String HEAD_URL = "https://mc-heads.net/avatar/";

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        FileConfiguration config = PlayerDeathCoords.getInstance().getConfig();
        Player p = e.getEntity();

        String username = p.getDisplayName();
        Location location = p.getLocation();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        String world = p.getWorld().getName();
        String deathMessage = e.getDeathMessage();

        int ping = p.getPing();
        int levels = p.getLevel();

        String message = String.format("**%s at %d, %d, %d.**", deathMessage, x, y, z);
        String deathcoords = String.format("&e%d %d %d", x, y, z);

        String pluginprefix = config.getString("prefix");
        String deathmsgyml = config.getString("messages.death-cords-message");

        DiscordWebhook webhook = PlayerDeathCoords.getInstance().getWebhook();

        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .addField("World", world, true)
                .addField("Coords", String.format("%d, %d, %d", x, y, z), true)
                .addField("Ping", String.format("%d", ping), true)
                .addField("XP", String.format("Lvl %d", levels), true)
                .setAuthor(deathMessage, "", HEAD_URL + p.getUniqueId().toString())
                .setColor(new Color(255 , 0, 0))
                .setDescription("")
                .setFooter("Player Death Coords v1.1.2", "")
    );


        if (config.getBoolean("messages.message-player")) {

            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.scheduleSyncDelayedTask(PlayerDeathCoords.getInstance(), new Runnable() {
                public void run() {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', pluginprefix + " " + deathmsgyml + String.format("%d, %d, %d", x, y, z)));
                }
            }, 15L);}

        try {
            webhook.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @EventHandler
    public void totemPop(EntityResurrectEvent e) {
        if (e.getEntity() instanceof Player p && e.getHand() != null) {

            String username = p.getDisplayName();
            DiscordWebhook webhook = PlayerDeathCoords.getInstance().getWebhook();

            webhook.addEmbed(new DiscordWebhook.EmbedObject()
                    .setAuthor(username + " used a totem to escape death!", "", HEAD_URL + p.getUniqueId().toString())
                    .setColor(new Color(255 , 143, 0))
            );

            FileConfiguration config = PlayerDeathCoords.getInstance().getConfig();
            String pluginprefix = config.getString("prefix");

            if (config.getBoolean("messages.broadcast-totems")) {
                BukkitScheduler scheduler = Bukkit.getScheduler();
                scheduler.scheduleSyncDelayedTask(PlayerDeathCoords.getInstance(), new Runnable() {
                    public void run() {
                        Bukkit.broadcastMessage(pluginprefix + username + " used a totem to escape death!");
                    }
                }, 1L);}


            try {
                webhook.execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
