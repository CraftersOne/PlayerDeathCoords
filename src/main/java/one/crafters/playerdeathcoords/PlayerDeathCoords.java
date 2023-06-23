package one.crafters.playerdeathcoords;

import one.crafters.playerdeathcoords.commands.CommandManagerClass;
import one.crafters.playerdeathcoords.events.DeathsAndRespawns;
import one.crafters.playerdeathcoords.events.PlayerCombatCheck;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerDeathCoords extends JavaPlugin {

    private static PlayerDeathCoords PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new DeathsAndRespawns(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerCombatCheck(), this);
        getCommand("pdc").setExecutor(new CommandManagerClass());
    }

    public DiscordWebhook getWebhook() {
            return new DiscordWebhook(getConfig().getString("webhook", ""));
    }


    public static PlayerDeathCoords getInstance() {
        return PLUGIN;
    }
}
