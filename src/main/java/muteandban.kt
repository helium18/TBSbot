import org.javacord.api.entity.user.User
import org.javacord.api.event.message.MessageCreateEvent

class muteandban {

    fun mute(event: MessageCreateEvent) {
        try {
            val mentionedUser: User = event.message.mentionedUsers[0];
            val userID = mentionedUser.idAsString;
            val messageAsList = event.messageContent.split(" ");

            if (!messageAsList[1].equals("<@!$userID>")) {

                throw InvalidSyntaxException("The mentionedUser should be at 2nd position! (or 1st Index)")

            } else {
                val reason = messageAsList.subList(2, messageAsList.size).joinToString(" ");

                // Giving the mute role
                val server = event.server.orElseThrow();
                val role = server.getRolesByName("Muted").get(0);

                // Muting
                mentionedUser.addRole(role, reason).join();

                event.channel.sendMessage("Muted dat nigga :triumph:")
            }

        } catch (exception: IndexOutOfBoundsException) {
            event.message.reply("Ass, whom are you trying to mute?! Mention someone");
        }

    }

    fun unmute(event: MessageCreateEvent) {
        try {
            val mentionedUser: User = event.message.mentionedUsers[0];
            val userID = mentionedUser.idAsString;
            val messageAsList = event.messageContent.split(" ");

            if (!messageAsList[1].equals("<@!$userID>")) {

                if (!messageAsList[1].equals(userID)) {
                    throw InvalidSyntaxException("The mentionedUser should be at 2nd position! (or 1st Index)");
                }

            } else {
                val reason = messageAsList.subList(2, messageAsList.size).joinToString(" ");

                // Giving the mute role
                val server = event.server.orElseThrow();
                val role = server.getRolesByName("Muted").get(0);

                // Unmuting
                mentionedUser.removeRole(role, reason).join();

                event.channel.sendMessage(":grin: did it.")
            }

        } catch (exception: IndexOutOfBoundsException) {
            event.message.reply("Ass, whom are you trying to mute?! Mention someone");
        } catch (exception: NoSuchElementException) {

            try {
                val userID = event.messageContent.split(" ")[1];
                val server = event.server.orElseThrow();
                val mentionedUser = server.getMemberById(userID).get();
                val messageAsList = event.messageContent.split(" ");

                val reason = messageAsList.subList(2, messageAsList.size).joinToString(" ");

                // Giving the mute role
                val role = server.getRolesByName("Muted").get(0);

                // Unmuting
                mentionedUser.removeRole(role, reason).join();

                event.channel.sendMessage(":grin: did it.")

            } catch (exception: NoSuchElementException) {
                event.message.reply("Ass, whom are you trying to mute?! Mention someone");
            }
        }

    }
}

class InvalidSyntaxException(message: String) : Exception(message)