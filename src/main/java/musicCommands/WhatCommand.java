package musicCommands;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import commands.Command;
import commands.Tag;
import discordBot.Data;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WhatCommand extends Command {
	
		public WhatCommand() {
			super("what", "Info o aktualnie granym utworze" ,new Tag[]{Tag.MUSICBOT});
		}

		@Override
		public void action(MessageReceivedEvent event, String[] args) throws FriendlyException{
			if(args[0].isEmpty()){
				MusicCommand.what(event.getGuild(), event.getTextChannel(),0);
				return;
			}
			try{
				int i = Integer.parseInt(args[0]);
				if(i < 0){
					event.getChannel().sendMessage("Nieprawid³owa wartoœæ.").queue();
					return;
				}
				MusicCommand.what(event.getGuild(), event.getTextChannel(),i);
			}catch(Exception ex){
				event.getChannel().sendMessage("Nieprawid³owa wartoœæ.").queue();
			}
		}	
		
		public void help(MessageReceivedEvent event) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.yellow);
			eb.setTitle(":white_check_mark:"+Data.PREFIX+"what");
			eb.setDescription("Poni¿ej znajdziesz szczegó³owy opis komendy.\n\n");
			eb.addField("Sposób 1:","Komenda:` "+Data.PREFIX+"what `\nPrzyk³ad:` "+Data.PREFIX+"what `\nPokazuje informacje o aktulanie odtwarzanym utworze.",false);
			eb.addField("Sposob 2:","Komenda:` "+Data.PREFIX+"what <numer w kolejce> `\nPrzyk³ad:` "+Data.PREFIX+"what 5 `\nPokazuje informacje o wybranym utowrze.",false);
			
			event.getChannel().sendMessage(eb.build()).queue();		
		}
}

