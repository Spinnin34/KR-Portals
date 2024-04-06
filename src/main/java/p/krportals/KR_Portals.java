package p.krportals;

import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import p.krportals.command.CommandHandler;
import p.krportals.facade.ConfigFacade;
import p.krportals.facade.MessagesFacade;
import p.krportals.facade.PluginFacade;
import p.krportals.listener.PortalListener;
import p.krportals.manager.SettingsManager;
import p.krportals.model.ConfigModel;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

public final class KR_Portals extends JavaPlugin {
    private static KR_Portals instance;
    private BukkitAudiences adventure;
    private PluginFacade pluginFacade;
    private ConfigFacade configFacade;
    private MessagesFacade messagesFacade;
    private MessageUtil messageUtil;

    public static void reloadPlugin() {
        instance.onDisable();
        instance.onEnable();
    }

    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);

        pluginFacade = new PluginFacade(instance, adventure());
//        commandList = new ArrayList<>();

        initSettings();
        messageUtil = new MessageUtil(messagesFacade);

        registerCommands();
        registerEvents();

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(instance);

        if (pluginFacade.getAdventure() != null) {
            pluginFacade.getAdventure().close();
            pluginFacade.setAdventure(null);
        }

        getLogger().info("Plugin disabled!");
    }

    public @NonNull BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    private void initSettings() {
        SettingsManager configManager = new SettingsManager(instance, "config.yml");
        ConfigModel configModel = new ConfigModel(configManager);
        configFacade = new ConfigFacade(configManager, configModel);

        SettingsManager messagesManager = new SettingsManager(instance, String.format("messages/messages_%s.yml", configModel.getLocale()), "messages/messages_es.yml");
        MessagesModel messagesModel = new MessagesModel(messagesManager);
        messagesFacade = new MessagesFacade(messagesManager, messagesModel);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PortalListener(pluginFacade, configFacade, messagesFacade, messageUtil), this);
    }

    private void registerCommands() {
        getServer().getPluginCommand("trueportals").setExecutor(new CommandHandler(pluginFacade, configFacade, messagesFacade, messageUtil));
        getCommand("trueportals").setTabCompleter(new CommandHandler(pluginFacade, configFacade, messagesFacade, messageUtil));
    }
}