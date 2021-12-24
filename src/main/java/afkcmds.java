import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class afkcmds {
    public void setAfk(MessageCreateEvent event) {
        utils util = new utils();

        Server server = event.getServer().orElseThrow();
        User user = event.getMessage().getAuthor().asUser().orElseThrow();
        String displayName = user.getDisplayName(server);

        String newDisplayName = "[AFK] " + displayName;
        user.updateNickname(server, newDisplayName).join();

        Properties properties = util.getConfig();

        String userID = event.getMessage().getUserAuthor().orElseThrow().getIdAsString();
        String afkReason = event.getMessageContent().substring(4);
        String userDetails = userID + ":" + afkReason;

        String afkUsers = properties.getProperty("AFK");
        afkUsers += userDetails + "|";

        properties.setProperty("AFK", afkUsers);
        util.setConfig(properties);

        event.getMessage().reply("Ok bro, continue rubbing");
    }

    public boolean isAfk(User user) {
        utils util = new utils();

        Properties properties = util.getConfig();

        String afkUsers = properties.getProperty("AFK");
        String[] afkUsersAsArray = afkUsers.split("\\|");

        String userID = user.getIdAsString();

        return (Arrays.stream(afkUsersAsArray).anyMatch(afkUser -> afkUser.contains(userID)));

    }

    public void updateAfk(User user, Server server) {
        String userID = user.getIdAsString();

        utils util = new utils();

        Properties properties = util.getConfig();

        String[] afkUsers = properties.getProperty("AFK").split("\\|");

        List<String> newAfkUsers = new ArrayList<String>();

        for (String afkUser : afkUsers) {
            if (!afkUser.contains(userID)) {
                newAfkUsers.add(afkUser);
            }
        }

        String afkUsersAsString = String.join("|", newAfkUsers);

        properties.setProperty("AFK", afkUsersAsString);

        util.setConfig(properties);

        String displayName = user.getDisplayName(server);
        String newDisplayName = displayName.replace("[AFK] ", "");

        user.updateNickname(server, newDisplayName).join();

        System.out.println("User Removed from afk");
    }

    public String getAfkMessage(User user) {
        String userID = user.getIdAsString();

        utils util = new utils();

        Properties properties = util.getConfig();

        String[] afkUsers = properties.getProperty("AFK").split("\\|");

        try {
            for (String afkUser : afkUsers) {
                if (afkUser.contains(userID)) {
                    return afkUser.split(":")[1];
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return String.format("I don't know, let's find out?<@%s>", userID);
        }

        return null;
    }

    public void getPrefix(MessageCreateEvent event) {
        utils util = new utils();

        String currentPrefix = util.getConfig().getProperty("PREFIX");

        event.getChannel().sendMessage(String.format("Call me by my prefix fuckturd: `%s`", currentPrefix));
    }
}
