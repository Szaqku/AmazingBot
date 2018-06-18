package utilityCommands;

import java.awt.Color;

import commands.Command;
import commands.Tag;
import discordBot.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VersionCommand extends Command {

	public VersionCommand() {
		super("version", "Wyœwietla aktualn¹ wersje Bota.", new Tag[]{Tag.UTILITY});
		
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.appendDescription(Data.VERSION);
		eb.setColor(Color.ORANGE);
		event.getChannel().sendMessage(eb.build()).queue();
	}

}
