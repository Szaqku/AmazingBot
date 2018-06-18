package funCommands;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import commands.Command;
import commands.Tag;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class KostkaCommand extends Command {

	Random rand;
	
	public KostkaCommand() {
		super("kostka", "Losuje liczbê z zakresu 1-6. Taka o kosteczka.", new Tag[]{Tag.FUN});
		rand = new Random();rand = new Random();
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args){
		event.getMessage().delete().queue();
		
		event.getChannel().sendMessage(event.getAuthor().getAsMention()+" rzuca kostk¹...").complete();
		event.getChannel().sendMessage("```Wyrzucasz liczbê oczek równ¹: "+(rand.nextInt(7)+1)+"```").completeAfter(2, TimeUnit.SECONDS);
	}
	
}
