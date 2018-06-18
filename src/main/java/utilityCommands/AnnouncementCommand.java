package utilityCommands;

import java.awt.Color;
import commands.Command;
import commands.Tag;
import discordBot.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AnnouncementCommand extends Command {
	
	
	public AnnouncementCommand() {	
		super("announce", "Tworzy ³adn¹ ramke z zawartoœci¹.", new Tag[]{Tag.UTILITY});
	}

	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		if(!event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)){
			event.getChannel().sendMessage("Your don't have permissions.").queue();

		}
		
		int len = args.length;
		String description = new String("");
		TextChannel channel = event.getTextChannel();
		Color color = null;
		
		for(int i = 1; i < len-4;i++){
			description+=args[i]+" ";
		}
		
		try{
			if(event.getGuild().getTextChannelsByName(args[len-1], true).isEmpty()){
				throw new Exception("Nieprawid³owa nazwa kana³u.");
			}else{
				channel = event.getGuild().getTextChannelsByName(args[len-1], true).get(0);
			}
			
			try{
				color = new Color(Integer.parseInt(args[len-4]),Integer.parseInt(args[len-3]),Integer.parseInt(args[len-2]));
			}
			catch(Exception e){
				throw new Exception("Nieprawid³owy kolor");
			}
			
			buildAnnouncementAndSend(args[0].replace("_"," "),description,color,event.getMessage().getIdLong(),channel);
		
		}catch(Exception ex){
			event.getChannel().sendMessage(ex.getMessage()).queue();
		}
	}

	@Override
	public void help(MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":white_check_mark:"+name);
		eb.setColor(Color.yellow);
		eb.addField("","Sposób 1:`"+Data.PREFIX+name+"` <`tytu³_wiadomoœci opis kolor(RGB) NazwaKana³u`> \nPrzyk³ad: "+Data.PREFIX+name+" jakiœ_tam_tytu³ tekst wolny od podkreœleñ 222 22 222 general`",false);

		event.getChannel().sendMessage(eb.build()).queue();		
	}
	
	private void buildAnnouncementAndSend(String title,String description,Color color,long idMessage,TextChannel channel){
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(title);
		eb.appendDescription(description);
		if(color != null)
			eb.setColor(color);
		
		channel.getGuild().getTextChannelsByName("konsola", true).get(0).getMessageById(idMessage).complete().delete().complete();
		channel.sendMessage(eb.build()).queue();
	}
}
