package p.krportals.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import p.krportals.facade.MessagesFacade;
import p.krportals.model.MessagesModel;

import java.util.Map;

public class MessageUtil {
    private final MessagesModel messagesModel;
    private final MiniMessage miniMessage;

    public MessageUtil(MessagesFacade messagesFacade) {
        this.messagesModel = messagesFacade.getMessagesModel();
        this.miniMessage = MiniMessage.miniMessage();
    }

    public Component create(String message) {
        String prefix = messagesModel.getPrefix();

        if (message == null) return Component.empty();

        return miniMessage.deserialize(message.replaceAll("%prefix%", prefix));
    }

    public Component create(String message, Map<String, String> placeholders) {
        String prefix = messagesModel.getPrefix();

        if (message == null) return Component.empty();

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "%" + entry.getKey() + "%";
            String replacement = entry.getValue();
            message = message.replace(placeholder, replacement);
        }

        return miniMessage.deserialize(message.replaceAll("%prefix%", prefix));
    }
}
