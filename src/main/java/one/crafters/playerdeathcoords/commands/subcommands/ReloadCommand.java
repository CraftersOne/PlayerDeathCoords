package one.crafters.playerdeathcoords.commands.subcommands;

import one.crafters.playerdeathcoords.PlayerDeathCoords;
import one.crafters.playerdeathcoords.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {


    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the configuration.";
    }

    @Override
    public String getSyntax() {
        return "/pdc reload";
    }

    @Override
    public void perform(Player player, String[] args) {

        FileConfiguration config = PlayerDeathCoords.getInstance().getConfig();
        String reloadmessage = config.getString("messages.reload-message");
        String pluginprefix = config.getString("prefix");

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadmessage
                .replace("{PREFIX}", pluginprefix)
        ));
        PlayerDeathCoords.getInstance().reloadConfig();
        PlayerDeathCoords.getInstance().saveConfig();

    }
}
