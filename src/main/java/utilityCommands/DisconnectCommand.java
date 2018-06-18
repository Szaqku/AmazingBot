package utilityCommands;

import commands.Command;
import commands.Tag;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DisconnectCommand extends Command{
	
	public DisconnectCommand() {
		super("disconnect","Roz³¹cz bota z serwerem.", new Tag[]{Tag.MUSICBOT});
	}

	@Override
	public void action(MessageReceivedEvent event, String[] args){
		MusicCommand.disconnect(event.getGuild(), event.getTextChannel(),event.getAuthor());
	}

}
