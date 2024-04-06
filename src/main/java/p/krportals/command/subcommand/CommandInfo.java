package p.krportals.command.subcommand;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import p.krportals.command.ICommand;
import p.krportals.facade.ConfigFacade;
import p.krportals.facade.MessagesFacade;
import p.krportals.model.ConfigModel;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandInfo implements ICommand {
    private final ConfigModel configModel;
    private final MessagesModel messagesModel;
    private final MessageUtil messageUtil;

    public CommandInfo(ConfigFacade configFacade, MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.configModel = configFacade.getConfigModel();
        this.messagesModel = messagesFacade.getMessagesModel();
        this.messageUtil = messageUtil;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getRegex() {
        return "";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{""};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.info";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        Map<String, String> statusPlaceholders = new HashMap<>();
        statusPlaceholders.put("end_portal_status", getState(configModel.isPortalsEnd()));
        statusPlaceholders.put("nether_portal_status", getState(configModel.isPortalsNether()));
        statusPlaceholders.put("end_destination_status", getState(configModel.getDestinationsEnd().getBoolean("enabled")));
        statusPlaceholders.put("nether_destination_status", getState(configModel.getDestinationsNether().getBoolean("enabled")));


        Component status = messageUtil.create(messagesModel.getStatusInfo(), statusPlaceholders);

        audience.sendMessage(status);

        return true;
    }

    public String getState(boolean state) {
        return messagesModel.getStatusInfoStates().get(!state ? 0 : 1);
    }
}
