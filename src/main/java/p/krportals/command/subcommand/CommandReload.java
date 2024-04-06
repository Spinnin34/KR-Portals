package p.krportals.command.subcommand;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import p.krportals.KR_Portals;
import p.krportals.command.ICommand;
import p.krportals.facade.MessagesFacade;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

public class CommandReload implements ICommand {
    private final MessagesModel messagesModel;
    private final MessageUtil messageUtil;

    public CommandReload(MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.messagesModel = messagesFacade.getMessagesModel();
        this.messageUtil = messageUtil;
    }

    @Override
    public String getName() {
        return "reload";
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
        return "trueportals.commands.reload";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {

        audience.sendMessage(messageUtil.create(messagesModel.getReloadInfo()));
        sender.sendMessage("§7§oBy the Developer ©Spinnin34");

        KR_Portals.reloadPlugin();

        return true;
    }
}
