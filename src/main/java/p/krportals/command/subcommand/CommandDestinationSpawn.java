package p.krportals.command.subcommand;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import p.krportals.command.ICommand;
import p.krportals.facade.ConfigFacade;
import p.krportals.model.ConfigModel;

import java.util.List;

public class CommandDestinationSpawn implements ICommand {
    private final ConfigModel configModel;


    public CommandDestinationSpawn(ConfigFacade configFacade) {
        this.configModel = configFacade.getConfigModel();
    }

    @Override
    public String getName() {
        return "destination";
    }

    @Override
    public String getRegex() {
        return "^(end|nether)\\s+(spawn)$";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{"end|nether", "spawn"};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.destination.spawn";
    }

    @Override
    public boolean isConsoleCan() {
        return false;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        Player player = (Player) sender;

        String dimension = args[0].toLowerCase();

        ConfigurationSection destinationSection = dimension.equals("end") ? configModel.getDestinationsEnd() : configModel.getDestinationsNether();
        String destinationWorld = destinationSection.getString("world");
        List<Double> destinationCoords = destinationSection.getDoubleList("coords");

        if (Bukkit.getWorld(destinationWorld) != null) {
            player.teleport(new Location(Bukkit.getWorld(destinationWorld), destinationCoords.get(0), destinationCoords.get(1), destinationCoords.get(2), destinationCoords.get(3).floatValue(), destinationCoords.get(4).floatValue()));
        }

        return true;
    }
}