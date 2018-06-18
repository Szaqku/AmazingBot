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
		super("play", "Dodaje utwór do kolejki", new Tag[]{Tag.MUSICBOT});
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
		eb.setDescription("Poni¿ej znajdziesz szczegó³owy opis komendy.");
		eb.addField("Sposób 1:","Komenda:` "+Data.PREFIX+"play <link do pliku> `\nPrzyk³ad:` "+Data.PREFIX+"play https://www.youtube.com/watch?v=JmCxwraaPRM `\nDodaje do kolejki utwór z linku.",false);
		eb.addField("Sposob 2:","Komenda:` "+Data.PREFIX+"play <tytu³ lub s³owa kluczowe> `\nPrzyk³ad:` "+Data.PREFIX+"play nie wiem `\nDodaje do kolejki utwór z podanym tytu³em.",false);
		
		event.getChannel().sendMessage(eb.build()).queue();		
	}
}
