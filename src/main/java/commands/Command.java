package commands;

import java.awt.Color;

import antlr.debug.MessageEvent;
import discordBot.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
	public String name;
	private String description;
	private Tag[] tags;
	
	public Command(String name, String description,Tag[] tags){
		this.name = name;
		this.description = description;
		this.setTags(tags);
	}

	public abstract void action(MessageReceivedEvent event,String[] args);

	public void help(MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":white_check_mark:"+name);
		eb.setColor(Color.yellow);
		eb.setDescription("Wpisz `"+Data.PREFIX+name+"`");
	
		event.getChannel().sendMessage(eb.build()).queue();		
	}
	public Tag[] getTags() {
		return tags;
	}
	public boolean checkTag(Tag tag){
		for(int i=0;i<tags.length;i++){
			if(tag.equals(tags[i]))
				return true;
		}
		return false;
	}
	public void setTags(Tag[] tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}
}
