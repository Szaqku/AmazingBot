package musicCommands;

import commands.Command;
import commands.Tag;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SkipCommand extends Command {

	public SkipCommand() {
		super("skip","Pomija utwór",new Tag[]{Tag.MUSICBOT});
	}

	@Override
	public void action(MessageReceivedEvent event, String[] args){
		MusicCommand.skip(event.getGuild(), event.getTextChannel());
	}			
}
	

