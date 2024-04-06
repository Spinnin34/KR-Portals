package p.krportals.command.subcommand;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import p.krportals.command.ICommand;
import p.krportals.facade.MessagesFacade;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

public class CommandHelp implements ICommand {
    private final MessagesModel messagesModel;
    private final MessageUtil messageUtil;

    public CommandHelp(MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.messagesModel = messagesFacade.getMessagesModel();
        this.messageUtil = messageUtil;
    }

    @Override
    public String getName() {
        return "help";
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
        return "trueportals.commands.help";
    }

    @Override
    public boolean isConsoleCan() {
        return true;
    }

    public boolean execute(CommandSender sender, Audience audience, String[] args) {
        audience.sendMessage(messageUtil.create(messagesModel.getHelpInfo()));
        sender.sendMessage("§7§oBy the Developer ©Spinnin34");

        return true;
    }
}
