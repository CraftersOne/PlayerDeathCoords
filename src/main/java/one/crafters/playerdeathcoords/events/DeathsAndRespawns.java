package one.crafters.playerdeathcoords.events;

import one.crafters.playerdeathcoords.DiscordWebhook;
import one.crafters.playerdeathcoords.PlayerDeathCoords;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;

public class DeathsAndRespawns implements Listener {


    private static final String HEAD_URL = "https://mc-heads.net/avatar/";

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
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

        DiscordWebhook webhook = PlayerDeathCoords.getInstance().getWebhook();

        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .addField("World", world, true)
                .addField("Location", String.format("%d, %d, %d", x, y, z), true)
                .addField("Ping", String.format("%d", ping), true)
                .addField("XP", String.format("Lvl %d", levels), true)
                .setAuthor(deathMessage, "", HEAD_URL + p.getUniqueId().toString())
                .setColor(new Color(255 , 0, 0))
                .setDescription("")
    );
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
            try {
                webhook.execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
