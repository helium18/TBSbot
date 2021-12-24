import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import javax.swing.*;
import java.util.*;

public class commands {

    public void changePrefix(MessageCreateEvent event, User bot) {
        utils utils = new utils();
        Properties properties = utils.getConfig();
        Server server = event.getServer().orElseThrow();
        String oldPrefix = utils.getConfig().getProperty("PREFIX");

        try {
            String currentName = bot.getDisplayName(server).replace(String.format("[ %s ] ", oldPrefix), "");

            String newPrefix = event.getMessageContent().split(" ")[1];

            if (newPrefix.length() != 1) {
                event.getMessage().reply("Invalid prefix. suck my dick. It can't be greater than 1 character (= 1 pp units)");
                return;
            }

            // Writing the new prefix to config file
            properties.setProperty("PREFIX", newPrefix);
            utils.setConfig(properties);

            bot.updateNickname(server, String.format("[ %s ] %s", newPrefix, currentName)).join();


            event.getMessage().reply("Updated the prefix!");

        } catch (IndexOutOfBoundsException exception) {
            event.getMessage().reply("where's the prefix?! retard");
        }

    }

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

        event.getMessage().reply(String.format("Call me by my prefix fuckturd: `%s`", currentPrefix));
    }
}
