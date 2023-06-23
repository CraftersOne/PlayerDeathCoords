package one.crafters.playerdeathcoords.events;

import one.crafters.playerdeathcoords.DiscordWebhook;
import one.crafters.playerdeathcoords.PlayerDeathCoords;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.awt.*;
import java.io.IOException;

public class PlayerCombatCheck implements Listener {

    private static final String HEAD_URL = "https://mc-heads.net/avatar/";

    FileConfiguration config = PlayerDeathCoords.getInstance().getConfig();

    @EventHandler
    public void onDamageOpponent(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player))
            return;
        if (!(e.getEntity() instanceof Player))
            return;

        Player victim = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();

        String victimname = victim.getName().toString();
        String attackername = attacker.getName().toString();

        double damage = e.getFinalDamage();
        String dmgtotalraw = Double.toString(damage);
        int dmgtotalint = Math.round(Float.parseFloat(dmgtotalraw));
        String dmg = Float.toString(dmgtotalint);

        String weapon = attacker
                .getInventory()
                .getItemInMainHand()
                .getType().toString()
                .toLowerCase()
                .replace("_", " ")
                .replace("air", "fist");

        String weaponname = weapon.substring(0, 1).toUpperCase() + weapon.substring(1);


        Location location = attacker.getLocation();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        String world = attacker.getWorld().getName()
                .replace("world_nether", "nether")
                .replace("world_the_end", "end")
                .replace("world", "overworld");
        String worldname = world.substring(0,1).toUpperCase() + world.substring(1);

        String playerattackmsg = config.getString("discord-messages.player-combat-message");

//        DiscordWebhook webhook = PlayerDeathCoords.getInstance().getWebhook();

        DiscordWebhook webhook = PlayerDeathCoords.getInstance().getWebhook();

        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setAuthor(playerattackmsg
                        .replace("{ATTACKER}", attackername)
                        .replace("{VICTIM}", victimname)
                        .replace("{DAMAGE}", dmg)
                        , "", HEAD_URL + attacker.getUniqueId().toString())
                        .addField("World", worldname, true)
                        .addField("Coords", String.format("%d, %d, %d", x, y, z), true)
                        .addField("Weapon", weaponname, true)
                .setColor(new Color(146, 146, 146))
        );

        try {
            webhook.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
