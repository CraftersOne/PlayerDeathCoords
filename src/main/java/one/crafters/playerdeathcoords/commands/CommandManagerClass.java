package one.crafters.playerdeathcoords.commands;

import one.crafters.playerdeathcoords.commands.subcommands.ReloadCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class CommandManagerClass implements CommandExecutor {

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManagerClass(){
        subcommands.add(new ReloadCommand());
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (sender instanceof Player){
            Player p = (Player) sender;

            if (sender.hasPermission("playerdeathcoords.reload")){

                if (args.length > 0){
                    for (int i = 0; i < getSubcommands().size(); i++){
                        if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                            getSubcommands().get(i).perform(p, args);
                        }
                    }
                }

            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fUnknown command. Type ''/help'' for help."));
            }
        } else {
            getLogger().info("You need to do this command in game.");
        }

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }
}
