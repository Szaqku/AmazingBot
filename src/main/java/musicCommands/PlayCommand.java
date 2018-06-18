package musicCommands;

import java.awt.Color;

import commands.Command;
import commands.Tag;
import discordBot.Data;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlayCommand extends Command{
	
	public PlayCommand() {
		super("play", "Dodaje utw�r do kolejki", new Tag[]{Tag.MUSICBOT});
	}
	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		MusicCommand.play(event.getGuild(), event.getTextChannel(), event.getAuthor(), args);
		System.out.println(String.join(" ",args));
	}
	public void help(MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.yellow);
		eb.setTitle(":white_check_mark:"+Data.PREFIX+"play");
		eb.setDescription("Poni�ej znajdziesz szczeg�owy opis komendy.");
		eb.addField("Spos�b 1:","Komenda:` "+Data.PREFIX+"play <link do pliku> `\nPrzyk�ad:` "+Data.PREFIX+"play https://www.youtube.com/watch?v=JmCxwraaPRM `\nDodaje do kolejki utw�r z linku.",false);
		eb.addField("Sposob 2:","Komenda:` "+Data.PREFIX+"play <tytu� lub s�owa kluczowe> `\nPrzyk�ad:` "+Data.PREFIX+"play nie wiem `\nDodaje do kolejki utw�r z podanym tytu�em.",false);
		
		event.getChannel().sendMessage(eb.build()).queue();		
	}
}
