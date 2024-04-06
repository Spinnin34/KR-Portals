package p.krportals.model;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import p.krportals.manager.SettingsManager;

import java.util.List;

public class MessagesModel {
    private final SettingsManager messagesManager;

    @Getter
    private String prefix;
    @Getter
    private String notPerms;
    @Getter
    private String needCorrectArgs;
    @Getter
    private String onlyPlayer;

    @Getter
    private String helpInfo;

    @Getter
    private String statusInfo;
    @Getter
    private List<String> statusInfoStates;

    @Getter
    private String statusChangeInfo;
    @Getter
    private List<String> statusChangePortals;
    @Getter
    private List<String> statusChangeStates;

    @Getter
    private String destinationChangeInfo;
    @Getter
    private List<String> destinationChangePortals;
    @Getter
    private List<String> destinationChangeStates;

    @Getter
    private String destinationSetSpawnInfo;
    @Getter
    private List<String> destinationSetSpawnPortals;

    @Getter
    private String statusCanNotUseEndTitle;
    @Getter
    private String statusCanNotUseEndSubtitle;
    @Getter
    private String statusCanNotUseNetherTitle;
    @Getter
    private String statusCanNotUseNetherSubtitle;

    @Getter
    private String reloadInfo;

    public MessagesModel(SettingsManager messagesManager) {
        this.messagesManager = messagesManager;
        reload();
    }

    public void reload() {
        YamlConfiguration messages = messagesManager.getSettings();

        this.prefix = messages.getString("prefix");
        this.notPerms = messages.getString("not-perms");
        this.needCorrectArgs = messages.getString("need-correct-args");
        this.onlyPlayer = messages.getString("only-player");

        this.helpInfo = messages.getString("help.info");

        this.statusInfo = messages.getString("status.info.info");
        this.statusInfoStates = messages.getStringList("status.info.states");

        this.statusChangeInfo = messages.getString("status.change.info");
        this.statusChangePortals = messages.getStringList("status.change.portals");
        this.statusChangeStates = messages.getStringList("status.change.states");

        this.destinationChangeInfo = messages.getString("destination.change.info");
        this.destinationChangePortals = messages.getStringList("destination.change.portals");
        this.destinationChangeStates = messages.getStringList("destination.change.states");

        this.destinationSetSpawnInfo = messages.getString("destination.setspawn.info");
        this.destinationSetSpawnPortals = messages.getStringList("destination.setspawn.portals");

        this.statusCanNotUseEndTitle = messages.getString("status.can-not-use.end.title");
        this.statusCanNotUseEndSubtitle = messages.getString("status.can-not-use.end.subtitle");
        this.statusCanNotUseNetherTitle = messages.getString("status.can-not-use.nether.title");
        this.statusCanNotUseNetherSubtitle = messages.getString("status.can-not-use.nether.subtitle");

        this.reloadInfo = messages.getString("reload.info");
    }
}
