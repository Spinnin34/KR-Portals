package p.krportals.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandManager {
    private final List<ICommand> commands;

    public CommandManager() {
        this.commands = new ArrayList<>();
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public List<String> getCommandNames() {
        return commands.stream().map(ICommand::getName).collect(Collectors.toList());
    }

    public Optional<ICommand> findCommandByName(String commandName) {
        return commands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(commandName)).findFirst();
    }

    public List<ICommand> findCommandsByName(String commandName) {
        return commands.stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(commandName))
                .collect(Collectors.toList());
    }

    public List<String> getCommandNamesForSender(CommandSender player) {
        return commands.stream().filter(cmd -> player.hasPermission(cmd.getPermission()) && !cmd.getName().isEmpty()).map(ICommand::getName).collect(Collectors.toList());
    }

    public List<ICommand> getCommandsForSender(CommandSender player) {
        return commands.stream().filter(cmd -> player.hasPermission(cmd.getPermission())).collect(Collectors.toList());
    }

    public void addCommand(ICommand command) {
        commands.add(command);

    }
}
