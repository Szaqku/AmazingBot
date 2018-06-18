package musicCommands;

import commands.Command;
import commands.Tag;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PreviousCommand extends Command{

	public PreviousCommand() {
		super("play", "Dodaje poprzednio odtwarzany utwór do kolejki.", new Tag[]{Tag.MUSICBOT});
	}
	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		MusicCommand.previous(event.getGuild());
	}
}
