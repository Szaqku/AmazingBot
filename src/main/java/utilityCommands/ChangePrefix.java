package utilityCommands;

import commands.Command;
import commands.Tag;
import discordBot.BotListener;
import discordBot.GuildSettings;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChangePrefix extends Command {

	public ChangePrefix() {
		super("prefix", "zmienia prefix dla bota.", new Tag[]{Tag.UTILITY});
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		if(!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR))
			return;
	
		String oldPrefix = BotListener.guilds.get(event.getGuild().getIdLong()).getPrefix();
		BotListener.guilds.put(Long.valueOf(event.getGuild().getId()),new GuildSettings(event.getGuild().getId(),args[0]));
		event.getGuild().getTextChannelsByName("konsola", true).get(0).sendMessage("Zmieniono prefix z **"+oldPrefix+"** na **"+args[0]+"**.").queue();
	}
}
