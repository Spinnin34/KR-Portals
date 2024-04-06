package p.krportals.command.subcommand;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import p.krportals.command.ICommand;
import p.krportals.facade.ConfigFacade;
import p.krportals.facade.MessagesFacade;
import p.krportals.manager.SettingsManager;
import p.krportals.model.ConfigModel;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandDestinationSetSpawn implements ICommand {
    private final SettingsManager configManager;
    private final ConfigModel configModel;
    private final MessagesModel messagesModel;
    private final MessageUtil messageUtil;


    public CommandDestinationSetSpawn(ConfigFacade configFacade, MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.configManager = configFacade.getConfigManager();
        this.configModel = configFacade.getConfigModel();
        this.messagesModel = messagesFacade.getMessagesModel();
        this.messageUtil = messageUtil;
    }

    @NotNull
    private double[] getCoords(Player player) {
        double playerCoordsX = player.getLocation().getX();
        double playerCoordsY = player.getLocation().getY();
        double playerCoordsZ = player.getLocation().getZ();
        double playerCoordsYaw = player.getLocation().getYaw();
        double playerCoordsPitch = player.getLocation().getPitch();

        return new double[]{
                roundCoords(playerCoordsX),
                roundCoords(playerCoordsY),
                roundCoords(playerCoordsZ),
                roundCoords(playerCoordsYaw),
                roundCoords(playerCoordsPitch)
        };
    }

    private double roundCoords(double coords) {
        return (double) Math.round(coords * 100) / 100;
    }

    @Override
    public String getName() {
        return "destination";
    }

    @Override
    public String getRegex() {
        return "^(end|nether)\\s+(setspawn)$";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{"end|nether", "setspawn"};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.destination.setspawn";
    }

    @Override
    public boolean isConsoleCan() {
        return false;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        Player player = (Player) sender;

        String playerWorld = player.getWorld().getName();
        double[] playerCoords = getCoords(player);

        String dimension = args[0].toLowerCase();

        Map<String, String> setSpawnDestinationPlaceholders = new HashMap<>();
        setSpawnDestinationPlaceholders.put("world_name", playerWorld);
        setSpawnDestinationPlaceholders.put("portal_name", getPortalName(dimension));
        setSpawnDestinationPlaceholders.put("coords_x", String.valueOf(playerCoords[0]));
        setSpawnDestinationPlaceholders.put("coords_y", String.valueOf(playerCoords[1]));
        setSpawnDestinationPlaceholders.put("coords_z", String.valueOf(playerCoords[2]));
        setSpawnDestinationPlaceholders.put("coords_yaw", String.valueOf(playerCoords[3]));
        setSpawnDestinationPlaceholders.put("coords_pitch", String.valueOf(playerCoords[4]));

        configManager.getSettings().set("destinations." + dimension + ".world", playerWorld);
        configManager.getSettings().set("destinations." + dimension + ".coords", playerCoords);
        configManager.save();

        configManager.reload();
        configModel.reload();

        Component destinationStatus = messageUtil.create(messagesModel.getDestinationSetSpawnInfo(), setSpawnDestinationPlaceholders);

        audience.sendMessage(destinationStatus);
        sender.sendMessage("§7§oBy the Developer ©Spinnin34");

        return true;
    }

    private String getPortalName(String dimension) {
        return messagesModel.getDestinationChangePortals().get("end".equals(dimension) ? 0 : 1);
    }
}