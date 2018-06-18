package musicCommands;

import commands.Command;
import commands.Tag;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCommand extends Command {

	public PauseCommand() {
		super("pause", "Pauzuje lub wznawia granie.", new Tag[]{Tag.MUSICBOT});
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		MusicCommand.pause(event.getGuild());
	}
	
}
