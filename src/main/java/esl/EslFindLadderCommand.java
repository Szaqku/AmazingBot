package esl;

import java.awt.Color;
import java.io.IOException;
import commands.Command;
import commands.Tag;
import discordBot.Data;
import esl.EslLadder.Group;
import esl.EslLadder.Ladder;
import esl.EslLadder.Pair;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class EslFindLadderCommand extends Command {

	public EslFindLadderCommand() {
		super("eslFindLadder", "Returns esl ladder.", new Tag[]{Tag.ESL});
	}
	@Override
	public void action(MessageReceivedEvent event, String[] args){
		if(event.getGuild().getMember(event.getAuthor()).hasPermission(Permission.ADMINISTRATOR)){
			try{
				int num = Integer.parseInt(args[1]);
				tournament tor;
				switch(args[0]){
					case "go4paladins":
						tor = tournament.GO4PALADINS;
						break;
					case "communityCup5v5":
						tor = tournament.COMUNITYCUP5V5;
						break;
					default: 
						tor = null;
				}
				if(tor == null){
					event.getChannel().sendMessage("Wrong syntax. Write "+Data.PREFIX+"help eslFindLadder").queue();
					return;
				}
				
				EslLadder el = new EslLadder(tor, num);
				EmbedBuilder[] ebTab = showLadder(el.ladder);
				if(ebTab!=null)
					for(int i = 0; i < ebTab.length;i++){
						event.getChannel().sendMessage(ebTab[i].build()).queue();
					}
				else
					event.getChannel().sendMessage("There isn't any ladder here. Wait few mins...").queue();
			}catch(IOException ex){
				event.getChannel().sendMessage("There isn't any ladder here. Wait few mins...").queue();
			}catch(NumberFormatException ex){
				event.getChannel().sendMessage("Wrong number you've sent...").queue();
			}
		}else{
			event.getChannel().sendMessage("You don't have permissions.").queue();
		}
		
	}	
	
	@Override
	public void help(MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.yellow);
		eb.setTitle(":white_check_mark:"+Data.PREFIX+"eslFindLadder");
		eb.setDescription("Poni¿ej znajdziesz szczegó³owy opis komendy.");
		eb.addField("Sposób 1:","Komenda:` "+Data.PREFIX+"esl <go4paladins or communityCup5v5> <num>`\nPrzyk³ad:` "+Data.PREFIX+"esl go4paladins 32`\n Shows ladder.",false);
		
		event.getChannel().sendMessage(eb.build()).queue();	
	}
	
	public EmbedBuilder[] showLadder(Ladder ladder){
		if(ladder.getRoundsListSize() == 0) 
			return null;
		
		EmbedBuilder[] ebTab = new EmbedBuilder[ladder.getRoundsListSize()];
		int n = 1;
		
		for(int i = 0; i < ladder.getRoundsListSize() && ladder.getRound(i) != null;i++){
			EmbedBuilder eb = new EmbedBuilder();
			
			if(ladder.getRound(i).getGroupsList().isEmpty())
				continue;
			
			for(Group group : ladder.getRound(i).getGroupsList()){
				if(group == null)
					continue;
				StringBuilder g = new StringBuilder();
				
				for(Pair pair : group.getPairsTable()){
					if(pair != null) 
						g.append("*"+pair.getTeam(0)+"* ** vs ** *"+pair.getTeam(1)+"* \n");
				}
				eb.addField("GROUP "+(n++), g.toString(), true);	
			}
			n = 1;
			if(i == ladder.getRoundsListSize()-2){
				eb.setTitle("Final round");
				eb.setColor(Color.YELLOW);
			}else if(i == ladder.getRoundsListSize()-1){
				eb.setTitle("Match for 3rd place");
				eb.setColor(Color.ORANGE);
			}else
				eb.setColor(Color.darkGray);
				eb.setTitle("Round "+i);
			ebTab[i] = eb;
		}
		return ebTab;
	}

}
