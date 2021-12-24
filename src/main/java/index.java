import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.Properties;

public class index {

    public static void main(String[] args) {

        // Getting the properties
        utils utils = new utils();
        Properties properties = utils.getConfig();

        commands cmd = new commands();

        // Log the bot in
        DiscordApi api = new DiscordApiBuilder().setToken(properties.getProperty("TOKEN")).login().join();

        // Bot
        User bot = api.getYourself();

        api.addMessageCreateListener(event -> {

            String messageContent = event.getMessageContent();
            String prefix = utils.getConfig().getProperty("PREFIX");
            User user = event.getMessage().getUserAuthor().orElseThrow();
            Server server = event.getServer().orElseThrow();
            boolean isUserAfk = cmd.isAfk(user);

            try {
                User mentionedUser = event.getMessage().getMentionedUsers().get(0);
                if (cmd.isAfk(mentionedUser)) {
                    if (!api.getYourself().getIdAsString().equals(event.getMessage().getAuthor().getIdAsString())) {
                        event.getChannel().sendMessage(String.format("Sar's afk 'cause: %s", cmd.getAfkMessage(mentionedUser)));
                    }
                }
            } catch (IndexOutOfBoundsException ignored) {
            }

            if (messageContent.startsWith(prefix)) {
                String command = event.getMessageContent().split(" ")[0].substring(1).toLowerCase();

                switch (command) {
                    case "changeprefix" -> cmd.changePrefix(event, bot);
                    case "afk" -> cmd.setAfk(event);
                }

            }
            if (isUserAfk) {
                event.getMessage().reply("How does it feel, tired? :yawning_face:");
                cmd.updateAfk(user, server);

            }

            String botID = "<@!920600262597566464>";
            if(event.getMessageContent().contains(botID)) {
            event.getChannel().sendMessage("No Way it worked");

            /*
            if (event.getMessageContent().contains("<@!920600262597566464>")) {
                cmd.getPrefix(event);
            }
            */

        });
    }

}