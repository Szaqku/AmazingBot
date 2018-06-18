package utilityCommands;

import commands.Command;
import commands.Tag;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PingCommand extends Command{

	public PingCommand() {
		super("ping", "Zwraca ping :D",new Tag[]{Tag.UTILITY});
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args){
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("Ping: "+event.getJDA().getPing());
		
		event.getTextChannel().sendMessage(eb.build()).queue();
	}
	
}
