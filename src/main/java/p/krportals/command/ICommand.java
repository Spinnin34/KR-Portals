package p.krportals.command;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public interface ICommand {
    String getName();

    String getRegex();

    String[] getCompleteArgs();

    String getPermission();

    boolean isConsoleCan();

    boolean execute(CommandSender sender, Audience audience, String[] args);
}
