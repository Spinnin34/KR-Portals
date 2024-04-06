package p.krportals.facade;

import lombok.Getter;
import p.krportals.manager.SettingsManager;
import p.krportals.model.MessagesModel;

@Getter
public class MessagesFacade {
    private final SettingsManager messagesManager;
    private final MessagesModel messagesModel;

    public MessagesFacade(SettingsManager messagesManager, MessagesModel messagesModel) {
        this.messagesManager = messagesManager;
        this.messagesModel = messagesModel;
    }
}
