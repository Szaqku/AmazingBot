package musicCommands;

import commands.Command;
import commands.Tag;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ClearCommand extends Command {
	
	public ClearCommand() {
		super("clear","Czyœci kolejke",new Tag[]{Tag.MUSICBOT});
	}

	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		MusicCommand.clear(event.getTextChannel());
	}		
}
