import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.Properties;

public class index {

    public static void main(String[] args) {

        // Getting the properties
        utils utils = new utils();
        Properties properties = utils.getConfig();

        commands cmd = new commands();

        // Log the bot in
        DiscordApi api = new DiscordApiBuilder()
                .setToken(properties.getProperty("TOKEN"))
                .login().join();

        api.addMessageCreateListener(event -> {

            String messageContent = event.getMessageContent();
            String prefix = utils.getConfig().getProperty("PREFIX");

            if (messageContent.startsWith(prefix)) {
                String command = event.getMessageContent().split(" ")[0].substring(1).toLowerCase();

                switch (command) {
                    case "changeprefix" -> cmd.changePrefix(event);
                }
            }
        });
    }

}