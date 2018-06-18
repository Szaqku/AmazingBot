package utilityCommands;

import java.awt.Color;
import java.util.Iterator;

import commands.Command;
import commands.Tag;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CleanCommand extends Command{

	public CleanCommand() {
		super("clean", "Usuwa wczeœniejsze wiadomosci.",new Tag[]{Tag.UTILITY}); 
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args){
		
		Iterator<Message> it = event.getGuild().getTextChannelsByName("konsola", true).get(0).getHistory().retrievePast(100).complete().iterator();
		while(it.hasNext()){
			Message mc = it.next();
			// if(mc.getAuthor().equals(event.getJDA().getSelfUser()))
				mc.delete().queue();;		
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("Wyczyszczone przez "+event.getAuthor().getAsMention());
		eb.setColor(Color.red);
		event.getTextChannel().sendMessage(eb.build()).queue();
	}
	
	 
}
