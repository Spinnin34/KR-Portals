package p.krportals.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import p.krportals.command.subcommand.*;
import p.krportals.facade.ConfigFacade;
import p.krportals.facade.MessagesFacade;
import p.krportals.facade.PluginFacade;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final BukkitAudiences adventure;
    private final ConfigFacade configFacade;
    private final MessagesFacade messagesFacade;
    private final MessagesModel messagesModel;
    private final MessageUtil messageUtil;
    private final CommandManager commandManager;

    public CommandHandler(PluginFacade pluginFacade, ConfigFacade configFacade, MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.adventure = pluginFacade.getAdventure();

        this.configFacade = configFacade;
        this.messagesFacade = messagesFacade;

        this.messagesModel = messagesFacade.getMessagesModel();

        this.messageUtil = messageUtil;

        this.commandManager = new CommandManager();

        initCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Audience audience = adventure.sender(sender);
        String[] slicedArgs = args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];

        Optional<ICommand> emptyCommand = commandManager.getCommands().stream().filter(cmd -> cmd.getName().isEmpty()).findFirst();

        if (args.length == 0) {
            if (!emptyCommand.isPresent()) {
                audience.sendMessage(messageUtil.create(messagesModel.getNeedCorrectArgs()));
                return true;
            }

            if (!(sender instanceof Player) && !emptyCommand.get().isConsoleCan()) {
                audience.sendMessage(messageUtil.create(messagesModel.getOnlyPlayer()));
                return true;
            }

            if (!sender.hasPermission(emptyCommand.get().getPermission())) {
                audience.sendMessage(messageUtil.create(messagesModel.getNotPerms()));
                return true;
            }
            return emptyCommand.map(cmd -> executeCommand(cmd, sender, audience, slicedArgs)).get();
        }

        List<ICommand> foundCommands = commandManager.findCommandsByName(args[0]);

        if (!foundCommands.isEmpty()) {
            for (ICommand commandObject : foundCommands) {
                if (Pattern.matches(commandObject.getRegex(), StringUtils.join(slicedArgs, " "))) {
                    if (!sender.hasPermission(commandObject.getPermission())) {
                        audience.sendMessage(messageUtil.create(messagesModel.getNotPerms()));
                        return true;
                    }
                    return executeCommand(commandObject, sender, audience, slicedArgs);
                }
            }
            audience.sendMessage(messageUtil.create(messagesModel.getNeedCorrectArgs()));
            return true;
        }

        audience.sendMessage(messageUtil.create(messagesModel.getNeedCorrectArgs()));
        return true;
    }

    private boolean executeCommand(ICommand command, CommandSender sender, Audience audience, String[] args) {
        return command.execute(sender, audience, args);
    }

    private void initCommands() {
        commandManager.addCommand(new CommandPortalStatus(configFacade, messagesFacade, messageUtil));
        commandManager.addCommand(new CommandDestinationStatus(configFacade, messagesFacade, messageUtil));
        commandManager.addCommand(new CommandDestinationSetSpawn(configFacade, messagesFacade, messageUtil));
        commandManager.addCommand(new CommandDestinationSpawn(configFacade));
        commandManager.addCommand(new CommandReload(messagesFacade, messageUtil));
        commandManager.addCommand(new CommandHelp(messagesFacade, messageUtil));
        commandManager.addCommand(new CommandInfo(configFacade, messagesFacade, messageUtil));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return commandManager.getCommandNamesForSender(sender);
        }

        String commandName = args[0];
        List<ICommand> foundCommands = commandManager.findCommandsByName(commandName);

        return foundCommands.stream()
                .filter(cmd -> sender.hasPermission(cmd.getPermission()) && !cmd.getName().isEmpty())
                .flatMap(cmd -> getCompletionList(args, cmd).stream())
                .collect(Collectors.toList());
    }

    private List<String> getCompletionList(String[] args, ICommand command) {
        int argIndex = args.length - 2;
        if (argIndex >= 0 && argIndex < command.getCompleteArgs().length) {
            return Arrays.asList(command.getCompleteArgs()[argIndex].split("\\|"));
        } else {
            return Collections.emptyList();
        }
    }
}