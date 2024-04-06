package p.krportals.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.scheduler.BukkitTask;
import p.krportals.KR_Portals;
import p.krportals.facade.ConfigFacade;
import p.krportals.facade.MessagesFacade;
import p.krportals.facade.PluginFacade;
import p.krportals.model.ConfigModel;
import p.krportals.model.MessagesModel;
import p.krportals.util.MessageUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.bukkit.World.Environment.THE_END;

public class PortalListener implements Listener {
    private final KR_Portals instance;
    private final BukkitAudiences adventure;
    private final MessagesModel messagesModel;
    private final ConfigModel configModel;
    private final MessageUtil messageUtil;
    private final Map<Player, Instant> playerList;
    private BukkitTask playerTask;

    public PortalListener(PluginFacade pluginFacade, ConfigFacade configFacade, MessagesFacade messagesFacade, MessageUtil messageUtil) {
        this.instance = pluginFacade.getInstance();
        this.adventure = pluginFacade.getAdventure();
        this.configModel = configFacade.getConfigModel();
        this.messagesModel = messagesFacade.getMessagesModel();
        this.messageUtil = messageUtil;
        this.playerList = new HashMap<>();
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();

        if (!configModel.isWorksPlayers()) return;

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();
        World.Environment fromWorld = event.getFrom().getWorld().getEnvironment();

        if (!player.hasPermission("trueportals.except.destination")) {
            if (changeWorld(player, toWorld)) {
                event.setCancelled(true);
                return;
            }
        }

        if (!player.hasPermission("trueportals.except.portal")) {
            if ((!configModel.isPortalsEnd() && toWorld == THE_END) ||
                    (!configModel.isPortalsNether() && (toWorld == World.Environment.NETHER || fromWorld == World.Environment.NETHER))) {
                event.setCancelled(true);

                if (playerList.isEmpty()) {
                    playerTask = startScheduler();
                }

                if (playerList.get(event.getPlayer()) == null) {
                    sendTitle(adventure.player(player), toWorld);
                }

                playerList.put(player, Instant.now());
            }
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (!configModel.isWorksEntities()) return;

        Entity entity = event.getEntity();

        World.Environment toWorld = event.getTo().getWorld().getEnvironment();
        World.Environment fromWorld = event.getFrom().getWorld().getEnvironment();

        if (changeWorld(entity, toWorld)) {
            event.setCancelled(true);
            return;
        }

        if ((!configModel.isPortalsEnd() && (toWorld == THE_END)) ||
                (!configModel.isPortalsNether() && (toWorld == World.Environment.NETHER || fromWorld == World.Environment.NETHER))) {
            event.setCancelled(true);
        }
    }

    private void sendTitle(Audience audience, World.Environment toWorld) {
        String titleMessage = toWorld == THE_END ? messagesModel.getStatusCanNotUseEndTitle() : messagesModel.getStatusCanNotUseNetherTitle();
        String subtitleMessage = toWorld == THE_END ? messagesModel.getStatusCanNotUseEndSubtitle() : messagesModel.getStatusCanNotUseNetherSubtitle();

        Component title = messageUtil.create(titleMessage);
        Component subtitle = messageUtil.create(subtitleMessage);

        Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        Title titleObj = Title.title(title, subtitle, times);

        audience.showTitle(titleObj);
    }

    private BukkitTask startScheduler() {
        int timeInSeconds = 5;
        long timeInMilliseconds = TimeUnit.SECONDS.toMillis(timeInSeconds);

        return Bukkit.getScheduler().runTaskTimer(instance, () -> {
            Instant currentTime = Instant.now();
            playerList.entrySet().removeIf(entry -> entry.getValue().isBefore(currentTime.plusMillis(timeInMilliseconds)));

            if (playerList.isEmpty() && playerTask != null) {
                playerTask.cancel();
                playerTask = null;
            }
        }, timeInSeconds * 20L, timeInSeconds * 20L);
    }

    private void teleportToWorld(Entity entity, String toWorld, List<Double> coords) {
        if (Bukkit.getWorld(toWorld) != null) {
            Location destination = new Location(Bukkit.getWorld(toWorld), coords.get(0), coords.get(1), coords.get(2), coords.get(3).floatValue(), coords.get(4).floatValue());
            entity.teleport(destination);
        }
    }

    private boolean changeWorld(Entity entity, World.Environment toWorld) {
        ConfigurationSection portalSection = toWorld == THE_END ? configModel.getDestinationsEnd() : configModel.getDestinationsNether();
        boolean portalEnabled = portalSection.getBoolean("enabled");
        String portalWorld = portalSection.getString("world");
        List<Double> portalCoords = portalSection.getDoubleList("coords");

        if (portalEnabled) {
            teleportToWorld(entity, portalWorld, portalCoords);
            return true;
        }

        return false;
    }
}
