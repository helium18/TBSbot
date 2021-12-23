import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Locale;
import java.util.Properties;

public class commands {

    public void changePrefix(MessageCreateEvent event) {
        utils utils = new utils();
        Properties properties = utils.getConfig();

        try {
            String newPrefix = event.getMessageContent().split(" ")[1];

            if (newPrefix.length() != 1) {
                event.getMessage().reply("Invalid prefix. suck my dick. It can't be greater than 1 character");
                return;
            }

            properties.setProperty("PREFIX", newPrefix);
            utils.setConfig(properties);
            event.getMessage().reply("Updated the prefix!");

        } catch (IndexOutOfBoundsException exception) {
            event.getMessage().reply("where's the prefix?! retard");
        }

    }
}
