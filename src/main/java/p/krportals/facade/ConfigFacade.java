package p.krportals.facade;

import lombok.Getter;
import p.krportals.manager.SettingsManager;
import p.krportals.model.ConfigModel;

@Getter
public class ConfigFacade {
    private final SettingsManager configManager;
    private final ConfigModel configModel;

    public ConfigFacade(SettingsManager configManager, ConfigModel configModel) {
        this.configManager = configManager;
        this.configModel = configModel;
    }
}
