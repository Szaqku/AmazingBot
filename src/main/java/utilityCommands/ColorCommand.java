package utilityCommands;

import java.awt.Color;

import commands.Command;
import commands.Tag;
import discordBot.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ColorCommand extends Command {

	public ColorCommand() {
		super("color", "kolorowoo", new Tag[]{});
	}
	
	@Override
	public void action(MessageReceivedEvent event, String[] args) {
		EmbedBuilder eb = new EmbedBuilder();
		int len = args.length;
		String description = new String("");
		for(int i = 1; i < len-3;i++){
			description+=args[i]+" ";
		}
		try{
			eb.setColor(new Color(Integer.parseInt(args[len-3]),Integer.parseInt(args[len-2]),Integer.parseInt(args[len-1])));
		}catch(Exception ex){
			event.getChannel().sendMessage("Niew³aœciwa watroœæ.").queue();
		}finally{
			eb.setTitle(args[0].replace("_", " "));
			eb.appendDescription(description);
			event.getMessage().delete().complete();
			event.getChannel().sendMessage(eb.build()).queue();
		}
	}

	@Override
	public void help(MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":white_check_mark:"+name);
		eb.setColor(Color.yellow);
		eb.addField("","Sposób 1:`"+Data.PREFIX+name+"` <`tytu³ opis (rgb)`> \nPrzyk³ad: "+Data.PREFIX+"color `Ko³o_jest_okrag³e opis jakis tam 255 23 233`",false);
		
		event.getChannel().sendMessage(eb.build()).queue();		
	}
	
}
