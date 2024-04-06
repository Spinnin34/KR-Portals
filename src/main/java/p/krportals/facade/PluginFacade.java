package p.krportals.facade;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import p.krportals.KR_Portals;

@Getter
public class PluginFacade {
    KR_Portals instance;
    @Setter
    BukkitAudiences adventure;

    public PluginFacade(KR_Portals instance, BukkitAudiences adventure) {
        this.instance = instance;
        this.adventure = adventure;
    }
}
