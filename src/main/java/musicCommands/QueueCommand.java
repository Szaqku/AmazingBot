package musicCommands;

import commands.Command;
import commands.Tag;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class QueueCommand extends Command{

	public QueueCommand() {
		super("queue", "Pokazuje listê utworów w kolejce.", new Tag[]{Tag.MUSICBOT});
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args){
		MusicCommand.queue(event.getGuild());
	}

}
