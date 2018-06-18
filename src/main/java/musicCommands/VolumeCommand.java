package musicCommands;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import commands.Command;
import commands.Tag;
import discordBot.Data;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VolumeCommand extends Command {

	public VolumeCommand() {
		super("volume","Wyœwietla poziom g³oœnoœci",new Tag[]{Tag.MUSICBOT});
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args) throws FriendlyException{
		Member user = event.getGuild().getMember(event.getAuthor());
		if(args[0].isEmpty()){
			MusicCommand.volume(event.getGuild(), event.getTextChannel(), args[0]);
			return;
		}
		if(user.hasPermission(Permission.ADMINISTRATOR)){		
			MusicCommand.volume(event.getGuild(), event.getTextChannel(), args[0]);
		}else{
			event.getChannel().sendMessage("Nie dla psa.. Nie masz odpowiednich uprawnieñ.").queue();
		}
	}
	public void help(MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.yellow);
		eb.addField("Sposób 1:","Komenda: `"+Data.PREFIX+"volume <0-200> `\nPrzyk³ad: `"+Data.PREFIX+"volume 25 `",false);
		
		event.getChannel().sendMessage(eb.build()).queue();		
	}
		
	
}
