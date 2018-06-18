package funCommands;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import commands.Command;
import commands.Tag;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class OrzelCommand extends Command {

	Random rand;
	
	public OrzelCommand() {
		super("moneta", "Orze³ czy reszka ?", new Tag[]{Tag.FUN});
		rand = new Random();
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args){
		event.getMessage().delete().complete();
		
		event.getChannel().sendMessage(event.getAuthor().getAsMention()+" rzuca monet¹...").complete();
		event.getChannel().sendMessage("```Wyrzucasz: "+((rand.nextBoolean())?"orze³ka```":"reszke```")).completeAfter(2, TimeUnit.SECONDS);
	}
	
}
