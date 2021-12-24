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

}
