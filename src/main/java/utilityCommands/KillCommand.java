package utilityCommands;

import java.awt.Color;
import commands.Command;
import commands.Tag;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class KillCommand extends Command{

	public KillCommand() {
		super("kill", "Wy³¹cza bota.",new Tag[]{Tag.UTILITY});
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args){
		if(event.getAuthor().equals(event.getJDA().getUserById("306083313301323776"))){
			EmbedBuilder eb = new EmbedBuilder();
			eb.setDescription("Wy³aczony przez "+event.getAuthor().getAsMention());
			eb.setColor(Color.orange);
			event.getTextChannel().sendMessage(eb.build()).queue();
			
			event.getJDA().shutdown();
		}
		else{
			EmbedBuilder eb = new EmbedBuilder();
			eb.setDescription("Kim ty w³aœciwie jesteœ, ¿eby mnie próbowaæ zabiæ ?");
			eb.setColor(Color.red);
			event.getTextChannel().sendMessage(eb.build()).queue();
		}
	}

}
