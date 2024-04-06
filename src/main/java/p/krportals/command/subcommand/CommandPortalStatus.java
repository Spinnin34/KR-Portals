package p.krportals.command.subcommand;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import p.krportals.command.ICommand;
import p.krportals.facade.ConfigFacade;
import p.krportals.facade.MessagesFacade;
import p.krportals.manager.SettingsManager;
import p.krportals.model.ConfigModel;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandPortalStatus implements ICommand {
    private final SettingsManager configManager;
    private final ConfigModel configModel;
    private final MessagesModel messagesModel;
    private final MessageUtil messageUtil;


    public CommandPortalStatus(ConfigFacade configFacade, MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.configManager = configFacade.getConfigManager();
        this.configModel = configFacade.getConfigModel();
        this.messagesModel = messagesFacade.getMessagesModel();
        this.messageUtil = messageUtil;
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getRegex() {
        return "^(end|nether)\\s+(enable|disable)$";
    }

    @Override
    public String[] getCompleteArgs() {
        return new String[]{"end|nether", "enable|disable"};
    }

    @Override
    public String getPermission() {
        return "trueportals.commands.portal.status";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        String dimension = args[0].toLowerCase();
        String status = args[1].toLowerCase();

        Map<String, String> changeStatusPlaceholders = new HashMap<>();
        changeStatusPlaceholders.put("portal_status", getPortalState(status));
        changeStatusPlaceholders.put("portal_name", getPortalName(dimension));

        configManager.getSettings().set("portals." + dimension, "enable".equals(status));
        configManager.save();

        configManager.reload();
        configModel.reload();

        Component changeStatus = messageUtil.create(messagesModel.getStatusChangeInfo(), changeStatusPlaceholders);

        audience.sendMessage(changeStatus);

        return true;
    }

    private String getPortalState(String status) {
        return messagesModel.getStatusChangeStates().get("disable".equals(status) ? 0 : 1);
    }

    private String getPortalName(String dimension) {
        return messagesModel.getStatusChangePortals().get("end".equals(dimension) ? 0 : 1);
    }
}
